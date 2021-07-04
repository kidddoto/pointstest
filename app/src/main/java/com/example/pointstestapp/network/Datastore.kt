package com.example.pointstestapp.network

import com.example.pointstestapp.model.PointsResponse

class Datastore {
    companion object {
        var points: PointsResponse? = null
    }

    //типа сохранение в бд
    fun savePoints(points: PointsResponse?) {
        Datastore.points = points
    }

    //и загрузка
    fun getPoints(): PointsResponse? {
        return Datastore.points
    }
}
