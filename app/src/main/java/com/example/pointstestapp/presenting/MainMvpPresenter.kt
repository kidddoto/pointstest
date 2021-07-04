package com.example.pointstestapp.presenting

import android.content.Context
import android.content.Intent
import com.example.pointstestapp.model.PointsResponse
import com.example.pointstestapp.network.ServerException
import com.example.pointstestapp.view.DataViewActivity
import com.example.pointstestapp.view.MainView
import com.example.pointstestapp.view.MvpView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result

class MainMvpPresenter : BasePresenter() {
    var view: MainView? = null
    val API_ENDPOINT =
        "https://hr-challenge.interactivestandard.com/api/test"

    override fun bindView(view: MvpView) {
        this.view = view as MainView
    }

    override fun unBindView(view: MvpView) {
        this.view = null
    }

    //val interactor = Interactor()
    fun fetchData(count: String) {
        val countInt: Int
        try {
            countInt = count.toInt()
            if (countInt == 0) {
                throw NumberFormatException("")
            }
        } catch (e: NumberFormatException) {
            //lalala
            view?.showError(e);
            return
        }
        view?.showLoading()
        Fuel.get(API_ENDPOINT + "/points/?count=${countInt}")
            .responseObject { request: Request, response: Response,
                              result: Result<PointsResponse, FuelError> ->
                view?.hideLoading()
                if (result.component1() != null) {
                    store.savePoints(result.component1())
                    val context = view as? Context
                    context?.let {
                        val intent = Intent(it, DataViewActivity::class.java)
                        it.startActivity(intent)
                    }
                } else {
                    val errorData =
                        result.component2()?.errorData?.let { String(it, Charsets.UTF_8) }
                    if (!errorData.isNullOrBlank()) {
                        view?.showError(
                            ServerException(
                                errorData
                            )
                        )
                    } else {
                        result.component2()?.cause?.let { view?.showError(it) }
                    }
                }
            }
    }
}