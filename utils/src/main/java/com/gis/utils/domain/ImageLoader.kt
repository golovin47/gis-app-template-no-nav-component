package com.gis.utils.domain

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView

interface ImageLoader {

  fun loadCircleImg(iv: ImageView, resId: Int, centerCrop: Boolean)

  fun loadImg(iv: ImageView, resId: Int, centerCrop: Boolean)

  fun loadImg(iv: ImageView, url: String, centerCrop: Boolean)

  fun loadImg(iv: ImageView, url: String, centerCrop: Boolean, corners:Int)

  fun loadImg(iv: ImageView, url: Uri, centerCrop: Boolean)

  fun loadBitmap(iv: ImageView, bitmap: Bitmap, centerCrop: Boolean)

  fun loadGif(iv: ImageView, url: String, centerCrop: Boolean)

  fun clear(iv: ImageView)
}