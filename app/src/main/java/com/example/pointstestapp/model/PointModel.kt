package com.example.pointstestapp.model

import java.io.Serializable

class PointModel : Serializable {
    val x: Float = 0.0f;
    val y: Float = 0.0f;

    override fun toString(): String {
        return "X: $x, Y: $y"
    }
}