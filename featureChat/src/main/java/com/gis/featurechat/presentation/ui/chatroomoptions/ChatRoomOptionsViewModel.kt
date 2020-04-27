package com.gis.featurechat.presentation.ui.chatroomoptions

import com.gis.utils.presentation.BaseViewModel
import com.gis.featurechat.presentation.ui.chatroomoptions.ChatRoomOptionsIntent.ShowDeleteConfirmationDialog
import com.gis.featurechat.presentation.ui.chatroomoptions.ChatRoomOptionsStateChange.*
import io.reactivex.Observable

class ChatRoomOptionsViewModel(private var showConfirmChatRoomDeleteDialog: ((Int) -> Unit)?) :
  BaseViewModel<ChatRoomOptionsState>() {

  override fun initState(): ChatRoomOptionsState = ChatRoomOptionsState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(
        intentStream.ofType(ShowDeleteConfirmationDialog::class.java)
          .doOnNext { event -> showConfirmChatRoomDeleteDialog?.invoke(event.chatId) }
          .map { CloseDialog }
      )
    )

  private fun handleError(e: Throwable) =
    Observable.just(Error(e), HideError)

  override fun reduceState(previousState: ChatRoomOptionsState, stateChange: Any): ChatRoomOptionsState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true, error = null)
      is CloseDialog -> previousState.copy(closeDialog = true)
      is Error -> previousState.copy(loading = false, error = stateChange.error)
      is HideError -> previousState.copy()
      else -> previousState
    }

  override fun onCleared() {
    showConfirmChatRoomDeleteDialog = null
    super.onCleared()
  }
}