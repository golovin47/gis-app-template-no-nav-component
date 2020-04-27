package com.gis.featurechat.presentation.ui.conversation

import android.net.Uri
import com.gis.repository.domain.entity.*
import com.gis.repository.domain.entity.ChatMessage.*

data class ConversationState(
  val loading: Boolean = false,
  val messageSent: Boolean = false,
  val openCamera: Boolean = false,
  val gifsOpened: Boolean = false,
  val name: String = "",
  val avatarUrl: String = "",
  val author: User = User.EMPTY,
  val users: List<User> = emptyList(),
  val messages: List<MessageListItem> = emptyList(),
  val gifs: List<GifListItem> = emptyList(),
  val gifsSearch: List<GifListItem> = emptyList(),
  val imageUri: Uri? = null,
  val error: Throwable? = null
)


sealed class ConversationIntent {
  class ObserveConversation(val id: Int) : ConversationIntent()
  object TakePhoto : ConversationIntent()
  object ChooseFromGallery : ConversationIntent()
  object ShowExtraPermissionsDialog : ConversationIntent()
  object GetPopularGifs : ConversationIntent()
  class GetNextGifsPage(val offset: Int) : ConversationIntent()
  class SearchGifs(val query: String, val offset: Int) : ConversationIntent()
  class GetNextSearchGifsPage(val query: String, val offset: Int) : ConversationIntent()
  class OpenGifs(val loadGifs: Boolean) : ConversationIntent()
  object CloseGifs : ConversationIntent()
  class ImageChosen(val uri: Uri) : ConversationIntent()
  class SendTextMessage(val chatTextMessage: ChatTextMessage) : ConversationIntent()
  class SendPhotoMessage(val chatPhotoMessage: ChatPhotoMessage) : ConversationIntent()
  class SendFileMessage(val chatFileMessage: ChatFileMessage) : ConversationIntent()
}


sealed class ConversationStateChange {
  object Idle : ConversationStateChange()
  object StartLoading : ConversationStateChange()
  object GifsOpened : ConversationStateChange()
  object GifsClosed : ConversationStateChange()
  object LoadingNextGifsPage : ConversationStateChange()
  object LoadingNextSearchGifsPage : ConversationStateChange()
  class UserReceived(val user: User) : ConversationStateChange()
  class GifsReceived(val gifs: List<GifListItem>) : ConversationStateChange()
  class SearchGifsReceived(val gifs: List<GifListItem>) : ConversationStateChange()
  class NextGifsPageReceived(val gifs: List<GifListItem>) : ConversationStateChange()
  class NextSearchGifsPageReceived(val gifs: List<GifListItem>) : ConversationStateChange()
  class OpenCamera(val uri: Uri) : ConversationStateChange()
  object MessageSent : ConversationStateChange()
  class ConversationReceived(val conversation: Conversation) : ConversationStateChange()
  class Error(val error: Throwable) : ConversationStateChange()
  object HideError : ConversationStateChange()
}


sealed class MessageListItem {
  class MessageItem(val chatMessage: ChatMessage) : MessageListItem()
  object MessageLoadingItem : MessageListItem()
}


sealed class GifListItem {
  class GifItem(val gif: Gif) : GifListItem()
  object GifLoadingItem : GifListItem()
  object GifSearchEmptyItem : GifListItem()
}