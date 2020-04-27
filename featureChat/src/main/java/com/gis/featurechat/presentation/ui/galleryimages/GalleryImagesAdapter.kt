package com.gis.featurechat.presentation.ui.galleryimages

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gis.utils.domain.ImageLoader
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featurechat.R
import com.gis.featurechat.databinding.ItemGalleryImageBinding
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImageListItem.GalleryImageItem
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImagesIntents.SelectImage
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImagesIntents.UnselectImage
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

private const val GALLERY_IMAGE_ITEM = 0x999

private const val UPDATE_SELECTION = 0x998

class GalleryImagesAdapter(
  private val intentsPublisher: PublishSubject<GalleryImagesIntents>,
  private val imageLoader: ImageLoader
) : ListAdapter<GalleryImageListItem, ViewHolder>(object : DiffUtil.ItemCallback<GalleryImageListItem>() {

  override fun areItemsTheSame(oldItem: GalleryImageListItem, newItem: GalleryImageListItem): Boolean =
    when {
      oldItem is GalleryImageItem && newItem is GalleryImageItem && oldItem.path == newItem.path -> true
      else -> false
    }

  override fun areContentsTheSame(oldItem: GalleryImageListItem, newItem: GalleryImageListItem): Boolean =
    when {
      oldItem is GalleryImageItem && newItem is GalleryImageItem && oldItem == newItem -> true
      else -> false
    }

  override fun getChangePayload(oldItem: GalleryImageListItem, newItem: GalleryImageListItem): Any? =
    if (newItem is GalleryImageItem && oldItem is GalleryImageItem) {
      when {
        newItem.selected != oldItem.selected -> UPDATE_SELECTION
        else -> Any()
      }
    } else Any()
}) {

  override fun getItemViewType(position: Int): Int {
    val item = getItem(position)

    return when (item) {
      is GalleryImageItem -> GALLERY_IMAGE_ITEM
      else -> -1
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    when (viewType) {
      GALLERY_IMAGE_ITEM -> GalleryImageViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_gallery_image,
          parent,
          false
        )
      )

      else -> GalleryImageViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_gallery_image,
          parent,
          false
        )
      )
    }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)

    if (holder is GalleryImageViewHolder && item is GalleryImageItem) {
      holder.bindGalleryImage(item, intentsPublisher, imageLoader)
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
    val item = getItem(position)

    if (holder is GalleryImageViewHolder && item is GalleryImageItem) {
      if (payloads.isEmpty()) {
        holder.bindGalleryImage(item, intentsPublisher, imageLoader)
      } else {
        for (payload in payloads) {
          when (payload) {
            UPDATE_SELECTION -> holder.updateSelection(item, intentsPublisher)
          }
        }
      }
    }
  }
}


class GalleryImageViewHolder(private val binding: ItemGalleryImageBinding) : ViewHolder(binding.root) {

  fun bindGalleryImage(
    item: GalleryImageItem,
    intentsPublisher: PublishSubject<GalleryImagesIntents>,
    imageLoader: ImageLoader
  ) {
    imageLoader.loadImg(binding.ivGalleryImage, item.path, false)
    if (item.selected) {
      binding.ivChecked.setBackgroundResource(R.drawable.shape_circle)
      binding.ivChecked.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
      binding.ivChecked.setImageResource(R.drawable.ic_check)
    } else {
      binding.ivChecked.setBackgroundResource(R.drawable.shape_circle_stroke)
      binding.ivChecked.setBackgroundColor(Color.TRANSPARENT)
      binding.ivChecked.setImageDrawable(null)
    }

    RxView.clicks(binding.itemRoot)
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .map { if (item.selected) UnselectImage(item.path) else SelectImage(item.path) }
      .subscribe(intentsPublisher)
  }

  fun updateSelection(item: GalleryImageItem, intentsPublisher: PublishSubject<GalleryImagesIntents>) {
    if (item.selected) {
      binding.ivChecked.setBackgroundResource(R.drawable.shape_circle)
      binding.ivChecked.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.colorPrimary))
      binding.ivChecked.setImageResource(R.drawable.ic_check)
    } else {
      binding.ivChecked.setBackgroundResource(R.drawable.shape_circle_stroke)
      binding.ivChecked.setBackgroundColor(Color.TRANSPARENT)
      binding.ivChecked.setImageDrawable(null)
    }

    RxView.clicks(binding.itemRoot)
      .throttleFirst(500, TimeUnit.MILLISECONDS)
      .map { if (item.selected) UnselectImage(item.path) else SelectImage(item.path) }
      .subscribe(intentsPublisher)
  }
}