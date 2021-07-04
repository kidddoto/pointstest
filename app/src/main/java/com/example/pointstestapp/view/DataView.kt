package com.example.pointstestapp.view

import com.example.pointstestapp.model.PointsResponse

interface DataView : MvpView {
    fun showData(value: PointsResponse)
    fun showSave(success: Boolean)
}