package com.gis.featurechat.presentation.ui.chatrooms

import com.gis.repository.domain.entity.ChatRoom

data class ChatRoomsState(
  val loading: Boolean = false,
  val showSearchField: Boolean = false,
  val chatRooms: List<ChatRoomsListItem> = emptyList(),
  val searchResult: List<ChatRoomsListItem> = emptyList(),
  val error: Throwable? = null
)


sealed class ChatRoomsIntent {
  object StartObserveChatRooms : ChatRoomsIntent()
  object RefreshChatRooms : ChatRoomsIntent()
  object ShowSearchField : ChatRoomsIntent()
  object CloseSearchField : ChatRoomsIntent()
  class SearchChatRooms(val search: String) : ChatRoomsIntent()
  class LoadNextChatRoomsPage(val pageNum: Int, val perPage: Int) : ChatRoomsIntent()
  class ShowChatRoomOptionsDialog(val chatId: Int) : ChatRoomsIntent()
  object GoToCreateNewRoom : ChatRoomsIntent()
  class GoToChatRoom(val chatId: Int) : ChatRoomsIntent()
}


sealed class ChatRoomsStateChange {
  object StartLoading : ChatRoomsStateChange()
  object FinishLoading : ChatRoomsStateChange()
  object RefreshFinished : ChatRoomsStateChange()
  object SearchFieldShown : ChatRoomsStateChange()
  object SearchFieldClosed : ChatRoomsStateChange()
  object StartLoadingNextPage : ChatRoomsStateChange()
  object FinishLoadingNextPage : ChatRoomsStateChange()
  class ChatRoomsReceived(val chatRooms: List<ChatRoomsListItem>) : ChatRoomsStateChange()
  class SearchResultReceived(val chatRooms: List<ChatRoomsListItem>) : ChatRoomsStateChange()

  class Error(val error: Throwable) : ChatRoomsStateChange()
  object HideError : ChatRoomsStateChange()
}


sealed class ChatRoomsListItem {
  data class ChatRoomsItem(val chatRoom: ChatRoom) : ChatRoomsListItem()
  object ChatRoomsLoadingItem : ChatRoomsListItem()
  object ChatRoomsEmptySearchItem : ChatRoomsListItem()
}