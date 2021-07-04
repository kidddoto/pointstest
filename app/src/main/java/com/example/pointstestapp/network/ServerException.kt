package com.example.pointstestapp.network

data class ServerException(val error: String) : Throwable(error)