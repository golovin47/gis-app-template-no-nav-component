package com.gis.featurechat.presentation.ui.chatroomoptions

data class ChatRoomOptionsState(
  val loading: Boolean = false,
  val closeDialog: Boolean = false,
  val error: Throwable? = null
)


sealed class ChatRoomOptionsIntent {
  class ShowDeleteConfirmationDialog(val chatId: Int) : ChatRoomOptionsIntent()
}


sealed class ChatRoomOptionsStateChange {
  object StartLoading : ChatRoomOptionsStateChange()
  object CloseDialog : ChatRoomOptionsStateChange()
  class Error(val error: Throwable) : ChatRoomOptionsStateChange()
  object HideError : ChatRoomOptionsStateChange()
}