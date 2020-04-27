package com.gis.featurechat.presentation.ui.messageimagepreview

import com.gis.repository.domain.entity.ChatMessage.*
import com.gis.repository.domain.entity.User

data class MessageImagePreviewState(
  val user: User = User.EMPTY,
  val error: Throwable? = null
)


sealed class MessageImagePreviewIntent {
  class SendPhotoMessage(val photoMessage: ChatPhotoMessage) : MessageImagePreviewIntent()
  object Cancel : MessageImagePreviewIntent()
}


sealed class MessageImagePreviewStateChange {
  class UserReceived(val user: User) : MessageImagePreviewStateChange()
  class Error(val error: Throwable) : MessageImagePreviewStateChange()
  object HideError : MessageImagePreviewStateChange()
}