package com.example.pointstestapp.presenting

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.pointstestapp.view.DataView
import com.example.pointstestapp.view.DataViewActivity
import com.example.pointstestapp.view.MvpView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class DataViewPresenter : BasePresenter() {

    var view: DataView? = null

    fun save(context: Context?, bitmap: Bitmap) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context?.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            view?.showSave(true)
        }
    }

    override fun bindView(view: MvpView) {
        this.view = view as DataView
        store.getPoints()?.let {
            this.view?.showData(it)
        }
    }

    override fun unBindView(view: MvpView) {
        this.view = null
    }
}