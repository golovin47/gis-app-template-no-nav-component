package com.gis.utils.data

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gis.utils.domain.ImageLoader

object GlideImageLoader : ImageLoader {

  override fun loadCircleImg(iv: ImageView, resId: Int, centerCrop: Boolean) {
    val options = RequestOptions().apply {
      if (centerCrop)
        centerCrop()
      circleCrop()
    }

    GlideApp.with(iv)
      .load(resId)
      .apply(options)
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .into(iv)
  }

  override fun loadImg(iv: ImageView, resId: Int, centerCrop: Boolean) {
    val options = RequestOptions().apply {
      if (centerCrop)
        centerCrop()
    }

    GlideApp.with(iv)
      .load(resId)
      .apply(options)
      .diskCacheStrategy(DiskCacheStrategy.ALL)
      .into(iv)
  }

  override fun loadImg(iv: ImageView, url: String, centerCrop: Boolean) {
    val options = RequestOptions().apply {
      if (centerCrop)
        centerCrop()
    }

    GlideApp.with(iv)
      .load(url)
      .apply(options)
      .into(iv)
  }

  override fun loadImg(iv: ImageView, url: String, centerCrop: Boolean, corners: Int) {
    val options = RequestOptions().apply {
      if (centerCrop)
        transforms(CenterCrop(), RoundedCorners(corners))
      else
        transforms(RoundedCorners(corners))
    }

    GlideApp.with(iv)
      .load(url)
      .apply(options)
      .into(iv)
  }

  override fun loadImg(iv: ImageView, url: Uri, centerCrop: Boolean) {
    val options = RequestOptions().apply {
      if (centerCrop)
        centerCrop()
    }

    GlideApp.with(iv)
      .load(url)
      .apply(options)
      .into(iv)
  }

  override fun loadBitmap(iv: ImageView, bitmap: Bitmap, centerCrop: Boolean) {
    val options = RequestOptions().apply {
      if (centerCrop)
        centerCrop()
    }

    GlideApp.with(iv)
      .load(bitmap)
      .apply(options)
      .into(iv)
  }

  override fun loadGif(iv: ImageView, url: String, centerCrop: Boolean) {
    val options = RequestOptions().apply {
      if (centerCrop)
        centerCrop()
    }

    GlideApp.with(iv)
      .load(url)
      .apply(options)
      .diskCacheStrategy(DiskCacheStrategy.DATA)
      .into(iv)
  }

  override fun clear(iv: ImageView) {
    GlideApp.with(iv)
      .clear(iv)
  }
}