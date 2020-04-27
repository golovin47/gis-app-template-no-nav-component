package com.gis.featurechat.presentation.ui.conversation

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.gis.featurechat.R
import kotlin.math.roundToInt

class MessagesItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

  private val topOffsetForDifferentMessages = context.resources.getDimension(R.dimen.top_offset_for_different_messages).roundToInt()
  private val topOffsetForSameMessages = context.resources.getDimension(R.dimen.top_offset_for_same_messages).roundToInt()
  private val bottomOffset = context.resources.getDimension(R.dimen.bottom_offset_for_latest_message).roundToInt()

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    val adapter = parent.adapter!!
    val itemCount = adapter.itemCount
    val curPosition = parent.getChildAdapterPosition(view)

    val topOffset = when {
      curPosition == itemCount - 1 -> topOffsetForDifferentMessages

      curPosition < itemCount - 1 -> {
        val curViewType = adapter.getItemViewType(curPosition)
        val nextViewType = adapter.getItemViewType(curPosition + 1)
        if (curViewType == nextViewType) topOffsetForSameMessages
        else topOffsetForDifferentMessages
      }

      else -> topOffsetForDifferentMessages
    }

    outRect.top = topOffset
    outRect.bottom = if (curPosition == 0) bottomOffset else 0
  }
}