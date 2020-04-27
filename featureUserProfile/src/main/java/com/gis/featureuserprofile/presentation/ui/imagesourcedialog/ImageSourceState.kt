package com.gis.featureuserprofile.presentation.ui.imagesourcedialog

import android.net.Uri

data class ImageSourceState(
  val closeDialog: Boolean = false,
  val openCamera: Boolean = false,
  val openGallery: Boolean = false,
  val imageUri: Uri? = null,
  val error: Throwable? = null
)


sealed class ImageSourceIntent {
  object TakePhoto : ImageSourceIntent()
  object ChooseFromGallery : ImageSourceIntent()
  class ImageChosen(val uri: Uri) : ImageSourceIntent()
  object ShowExtraPermissionsDialog : ImageSourceIntent()
  object Cancel : ImageSourceIntent()
}


sealed class ImageSourceStateChange {
  object Idle : ImageSourceStateChange()
  class OpenCamera(val uri: Uri) : ImageSourceStateChange()
  object OpenGallery : ImageSourceStateChange()
  object CloseDialog : ImageSourceStateChange()
  class Error(val error: Throwable) : ImageSourceStateChange()
  object HideError : ImageSourceStateChange()
}