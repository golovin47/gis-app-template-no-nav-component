package com.gis.featurechat.presentation.ui.galleryimages

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import com.gis.featurechat.R
import kotlin.math.roundToInt

class GalleryImagesItemDecoration(context: Context) : ItemDecoration() {

  private val galleryImagesOffset = context.resources.getDimension(R.dimen.gallery_images_offset).roundToInt()

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
    val curPosition = parent.getChildAdapterPosition(view)
    val itemCount = parent.adapter!!.itemCount

    val leftOffset: Int
    val topOffset: Int = galleryImagesOffset
    val rightOffset: Int
    val bottomOffset: Int

    if ((curPosition + 1) % 2 != 0) {
      leftOffset = galleryImagesOffset
      rightOffset = galleryImagesOffset / 2
    } else {
      leftOffset = galleryImagesOffset / 2
      rightOffset = galleryImagesOffset
    }

    bottomOffset = when {
      itemCount % 2 == 0 && curPosition >= itemCount - 2 -> galleryImagesOffset
      itemCount % 2 != 0 && curPosition == itemCount - 1 -> galleryImagesOffset
      else -> 0
    }

    outRect.left = leftOffset
    outRect.top = topOffset
    outRect.right = rightOffset
    outRect.bottom = bottomOffset
  }
}