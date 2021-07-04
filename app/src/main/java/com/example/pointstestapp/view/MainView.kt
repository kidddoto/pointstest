package com.example.pointstestapp.view

import com.example.pointstestapp.model.PointsResponse

interface MainView : MvpView {
    fun showLoading()
    fun hideLoading()
}