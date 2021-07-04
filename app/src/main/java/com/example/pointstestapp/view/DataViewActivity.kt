package com.example.pointstestapp.view

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import com.example.pointstestapp.R
import com.example.pointstestapp.common.Helpers
import com.example.pointstestapp.model.PointModel
import com.example.pointstestapp.model.PointsResponse
import com.example.pointstestapp.presenting.BasePresenter
import com.example.pointstestapp.presenting.DataViewPresenter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import com.permissionx.guolindev.PermissionX
import java.util.*


class DataViewActivity : BaseViewActivity(), DataView {
    lateinit var chart: LineChart
    lateinit var chBezier: CheckBox
    lateinit var presenter: DataViewPresenter
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_view)

        //аналогично =) butterknife
        chart = findViewById(R.id.chart1)
        chart.setBackgroundColor(Helpers.getThemeColor(this, R.attr.colorOnPrimary))
        //супер-цвет!
        chart.setNoDataTextColor(Helpers.getThemeColor(this, R.attr.colorSecondary))
        chart.setTouchEnabled(true)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        //chart.setPinchZoom(false)
        chart.description.isEnabled = false
        chart.invalidate()
        chart.setDrawGridBackground(false)

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            PermissionX.init(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request { allGranted, _, _ ->
                    if (allGranted) {
                        presenter.save(this, chart.chartBitmap)
                    }
                }
        }
        chBezier = findViewById(R.id.chBezier)
        chBezier.setOnCheckedChangeListener { buttonView, isChecked ->
            updateBezier()
        }
        listView = findViewById(R.id.listView)

        presenter = BasePresenter.bindPresenter(this, DataViewPresenter::class) as DataViewPresenter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unBindView(this)
    }

    override fun showData(value: PointsResponse) {
        listView.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, value.points)

        val viewData = value.points.map {
            Entry(it.x, it.y)
        }
        Collections.sort(viewData, EntryXComparator())

        var viewSet = LineDataSet(viewData, "CoolSet😎")
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            viewSet = chart.data.getDataSetByIndex(0) as LineDataSet
            viewSet.mode =
                if (chBezier.isChecked) LineDataSet.Mode.CUBIC_BEZIER else LineDataSet.Mode.LINEAR
            viewSet.values = viewData
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            viewSet.axisDependency = YAxis.AxisDependency.LEFT
            viewSet.color = Helpers.getThemeColor(this, R.attr.colorSecondary)
            viewSet.setDrawCircles(true)
            viewSet.lineWidth = 2f
            viewSet.circleRadius = 3f
            viewSet.fillAlpha = 255
            viewSet.setDrawFilled(false)
            viewSet.fillColor = Color.WHITE
            viewSet.setDrawCircleHole(false)
            viewSet.setDrawHorizontalHighlightIndicator(false)
            viewSet.highLightColor = Helpers.getThemeColor(this, R.attr.colorPrimary)
            viewSet.mode =
                if (chBezier.isChecked) LineDataSet.Mode.CUBIC_BEZIER else LineDataSet.Mode.LINEAR
            val data = LineData(mutableListOf<ILineDataSet>(viewSet))
            data.setDrawValues(true)
            chart.data = data
        }

        chart.invalidate()
    }

    override fun showSave(success: Boolean) {
        Toast.makeText(
            this,
            if (success) getString(R.string.save_success) else getString(R.string.save_fail),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun updateBezier() {
        try {
            val sets = chart.data.dataSets
            for (iSet in sets) {
                val set = iSet as LineDataSet
                set.mode =
                    if (chBezier.isChecked) LineDataSet.Mode.CUBIC_BEZIER else LineDataSet.Mode.LINEAR
            }
            chart.invalidate()
        } catch (ignored: java.lang.Exception) {

        }
    }
}