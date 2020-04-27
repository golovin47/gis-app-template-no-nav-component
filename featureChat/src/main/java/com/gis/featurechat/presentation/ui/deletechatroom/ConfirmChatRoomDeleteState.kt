package com.gis.featurechat.presentation.ui.deletechatroom

data class ConfirmChatRoomDeleteState(
  val loading: Boolean = false,
  val closeDialog: Boolean = false,
  val error: Throwable? = null
)


sealed class ConfirmChatRoomDeleteIntent {
  class DeleteChatRoom(val chatId: Int) : ConfirmChatRoomDeleteIntent()
  object Cancel : ConfirmChatRoomDeleteIntent()
}


sealed class ConfirmChatRoomDeleteStateChange {
  object StartLoading : ConfirmChatRoomDeleteStateChange()
  object CloseDialog : ConfirmChatRoomDeleteStateChange()
  class Error(val error: Throwable) : ConfirmChatRoomDeleteStateChange()
  object HideError : ConfirmChatRoomDeleteStateChange()
}