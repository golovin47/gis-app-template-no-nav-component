package com.gis.featurechat.presentation.ui.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gis.repository.domain.entity.ChatMessage.ChatPhotoMessage
import com.gis.repository.domain.entity.User
import com.gis.utils.databinding.EmptySearchPlaceholderBinding
import com.gis.utils.domain.ImageLoader
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featurechat.R
import com.gis.featurechat.databinding.ItemGifBinding
import com.gis.featurechat.databinding.ItemGifLoadingBinding
import com.gis.featurechat.presentation.ui.conversation.ConversationIntent.SendPhotoMessage
import com.gis.featurechat.presentation.ui.conversation.GifListItem.*
import io.reactivex.subjects.PublishSubject
import java.util.*
import java.util.concurrent.TimeUnit

private const val GIF_ITEM = 0x999
private const val GIF_LOADING_ITEM = 0x998
private const val GIF_EMPTY_SEARCH_ITEM = 0x997

private const val UPDATE_URL = 0x996

class GifsAdapter(
  var user:User = User.EMPTY,
  private val imageLoader: ImageLoader,
  private val intentsPublisher: PublishSubject<ConversationIntent>
) : ListAdapter<GifListItem, ViewHolder>(object : DiffUtil.ItemCallback<GifListItem>() {

  override fun areItemsTheSame(oldItem: GifListItem, newItem: GifListItem): Boolean =
    when {
      oldItem is GifItem && newItem is GifItem && oldItem.gif.id == newItem.gif.id -> true
      oldItem is GifLoadingItem && newItem is GifLoadingItem -> true
      oldItem is GifSearchEmptyItem && newItem is GifSearchEmptyItem -> true
      else -> false
    }

  override fun areContentsTheSame(oldItem: GifListItem, newItem: GifListItem): Boolean =
    when {
      oldItem is GifItem && newItem is GifItem && oldItem.gif == newItem.gif -> true
      oldItem is GifLoadingItem && newItem is GifLoadingItem -> true
      oldItem is GifSearchEmptyItem && newItem is GifSearchEmptyItem -> true
      else -> false
    }

  override fun getChangePayload(oldItem: GifListItem, newItem: GifListItem): Any? =
    if (oldItem is GifItem && newItem is GifItem) {
      when {
        oldItem.gif.url != newItem.gif.url -> UPDATE_URL
        else -> Any()
      }
    } else {
      Any()
    }
}) {

  override fun getItemViewType(position: Int): Int {
    val item = getItem(position)
    return when (item) {
      is GifItem -> GIF_ITEM
      is GifSearchEmptyItem -> GIF_EMPTY_SEARCH_ITEM
      else -> GIF_LOADING_ITEM
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    when (viewType) {
      GIF_ITEM -> GifViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_gif, parent, false)
      )
      GIF_EMPTY_SEARCH_ITEM -> GifSearchEmptyViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.empty_search_placeholder, parent, false)
      )
      else -> GifLoadingViewHolder(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_gif_loading, parent, false)
      )
    }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)

    when (item) {
      is GifItem -> (holder as GifViewHolder).bind(user, item, imageLoader, intentsPublisher)
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
    val item = getItem(position)

    if (item is GifItem && holder is GifViewHolder) {
      if (payloads.isNotEmpty()) {
        for (payload in payloads) {
          when (payload) {
            UPDATE_URL -> holder.updateUrl(item.gif.url, imageLoader)
          }
        }
      } else
        holder.bind(user,item, imageLoader, intentsPublisher)
    }
  }
}

class GifViewHolder(private val binding: ItemGifBinding) : ViewHolder(binding.root) {

  fun bind(user:User,item: GifItem, imageLoader: ImageLoader, intentsPublisher: PublishSubject<ConversationIntent>) {
    imageLoader.loadGif(binding.ivGif, item.gif.url, true)

    RxView.clicks(binding.itemRoot)
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .map {
        SendPhotoMessage(
          ChatPhotoMessage(
            id = System.currentTimeMillis().toInt(),
            imageUrl = item.gif.url,
            author = user,
            date = Date(),
            loading = false,
            success = false,
            error = false
          )
        )
      }
      .subscribe(intentsPublisher)
  }

  fun updateUrl(url: String, imageLoader: ImageLoader) {
    imageLoader.loadGif(binding.ivGif, url, true)
  }
}

class GifLoadingViewHolder(binding: ItemGifLoadingBinding) : ViewHolder(binding.root)

class GifSearchEmptyViewHolder(binding: EmptySearchPlaceholderBinding) : ViewHolder(binding.root)