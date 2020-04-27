package com.gis.featureuserprofile.presentation.ui.userimagepreview

import com.gis.repository.domain.entity.User


data class UserImagePreviewState(
  val loading: Boolean = false,
  val user: User? = null,
  val error: Throwable? = null
)


sealed class UserImagePreviewIntent {
  class UpdateAvatar(val user: User) : UserImagePreviewIntent()
  object Cancel : UserImagePreviewIntent()
}


sealed class UserImagePreviewStateChange {
  object StartLoading : UserImagePreviewStateChange()
  class UserReceived(val user: User) : UserImagePreviewStateChange()
  object Success : UserImagePreviewStateChange()
  class Error(val error: Throwable) : UserImagePreviewStateChange()
  object HideError : UserImagePreviewStateChange()
}