package com.gis.featurechat.presentation.ui.galleryimages

import com.gis.repository.domain.entity.ChatMessage.*
import com.gis.repository.domain.entity.User

data class GalleryImagesState(
  val loading: Boolean = false,
  val user: User = User.EMPTY,
  val images: List<GalleryImageListItem> = emptyList(),
  val selectedCount: Int = 0,
  val error: Throwable? = null
)


sealed class GalleryImagesIntents {
  object StartObserve : GalleryImagesIntents()
  object GetGalleryImages : GalleryImagesIntents()
  class SelectImage(val path: String) : GalleryImagesIntents()
  class UnselectImage(val path: String) : GalleryImagesIntents()
  class SendImages(val images: List<ChatPhotoMessage>) : GalleryImagesIntents()
  object Cancel : GalleryImagesIntents()
}


sealed class GalleryImagesStateChange {
  object Idle : GalleryImagesStateChange()
  object ObservingStarted : GalleryImagesStateChange()
  class UserReceived(val user: User) : GalleryImagesStateChange()
  object StartLoading : GalleryImagesStateChange()
  class GalleryImagesReceived(val images: List<GalleryImageListItem>) : GalleryImagesStateChange()
  class ImageSelected(val path: String) : GalleryImagesStateChange()
  class ImageUnselected(val path: String) : GalleryImagesStateChange()
  class Error(val error: Throwable) : GalleryImagesStateChange()
  object HideError : GalleryImagesStateChange()
}


sealed class GalleryImageListItem {
  data class GalleryImageItem(
    val path: String,
    val selected: Boolean
  ) : GalleryImageListItem()
}