package com.example.pointstestapp.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.pointstestapp.R
import com.example.pointstestapp.model.PointsResponse
import com.example.pointstestapp.network.ServerException
import com.example.pointstestapp.presenting.BasePresenter
import com.example.pointstestapp.presenting.MainMvpPresenter
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : BaseViewActivity(), MainView {

    //самодельный для наглядности, а так moxy
    lateinit var presenter: MainMvpPresenter
    lateinit var btnGo: Button
    lateinit var edPointsCount: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGo = findViewById(R.id.goButton)
        btnGo.setOnClickListener {
            onButtonClick()
        }

        //тут можно было бы еще дизейблить кнопку при пустом поле...
        edPointsCount = findViewById(R.id.edPointsCount)
        edPointsCount.imeOptions = EditorInfo.IME_ACTION_DONE;
        edPointsCount.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onButtonClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false;
        }

        presenter = BasePresenter.bindPresenter(this, MainMvpPresenter::class) as MainMvpPresenter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unBindView(this)
    }

    private fun onButtonClick() {
        hideKeyboard(this)
        presenter.fetchData(edPointsCount.text.toString())
    }

    override fun showLoading() {
        btnGo.showProgress {

        }
        btnGo.isEnabled = false
    }

    override fun hideLoading() {
        btnGo.hideProgress(R.string.go_title_done)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                btnGo.text = getString(R.string.go_title)
            }, TimeUnit.SECONDS.toMillis(1)
        )
        btnGo.isEnabled = true
    }

    override fun showError(error: Throwable) {
        when (error) {
            is java.lang.NumberFormatException -> {
                Toast.makeText(this, getString(R.string.invalid_points_count), Toast.LENGTH_SHORT)
                    .show()
            }
            is ServerException -> {
                Toast.makeText(this, error.error, Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
            }
        }
    }
}