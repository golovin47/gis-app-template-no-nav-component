package com.gis.featurechat.presentation.ui.conversation

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import com.gis.featurechat.R
import kotlin.math.roundToInt

class GifsItemDecoration(context: Context) : ItemDecoration() {

  private val gifEdgeOffset = context.resources.getDimension(R.dimen.item_gif_edge_offset).roundToInt()
  private val gifOffset = context.resources.getDimension(R.dimen.item_gif_offset).roundToInt()

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
    val curPosition = parent.getChildAdapterPosition(view)

    val leftOffset = if (curPosition == 0) gifEdgeOffset else gifOffset
    val rightOffset = if (curPosition == parent.adapter!!.itemCount - 1) gifEdgeOffset else gifOffset

    outRect.left = leftOffset
    outRect.top = gifEdgeOffset
    outRect.right = rightOffset
    outRect.bottom = gifEdgeOffset
  }
}