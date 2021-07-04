package com.example.pointstestapp.common

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt


class Helpers {
    companion object {
        @ColorInt
        fun getThemeColor(
            context: Context,
            @AttrRes attributeColor: Int
        ): Int {
            val value = TypedValue()
            context.theme.resolveAttribute(attributeColor, value, true)
            return value.data
        }
    }
}