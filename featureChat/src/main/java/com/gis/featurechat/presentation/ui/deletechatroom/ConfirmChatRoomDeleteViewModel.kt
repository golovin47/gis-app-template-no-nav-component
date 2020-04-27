package com.gis.featurechat.presentation.ui.deletechatroom

import com.gis.repository.domain.interactors.DeleteChatRoomUseCase
import com.gis.utils.presentation.BaseViewModel
import io.reactivex.Observable
import com.gis.featurechat.presentation.ui.deletechatroom.ConfirmChatRoomDeleteIntent.*
import com.gis.featurechat.presentation.ui.deletechatroom.ConfirmChatRoomDeleteStateChange.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ConfirmChatRoomDeleteViewModel(private val deleteChatRoomUseCase: DeleteChatRoomUseCase) :
  BaseViewModel<ConfirmChatRoomDeleteState>() {

  override fun initState(): ConfirmChatRoomDeleteState = ConfirmChatRoomDeleteState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(
        intentStream.ofType(DeleteChatRoom::class.java)
          .switchMap { event ->
            deleteChatRoomUseCase.execute(event.chatId)
              .toSingleDefault(CloseDialog)
              .toObservable()
              .cast(ConfirmChatRoomDeleteStateChange::class.java)
              .startWith(StartLoading)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(Cancel::class.java)
          .map { CloseDialog }
      )
    )

  private fun handleError(e: Throwable) =
    Observable.just(Error(e), HideError)

  override fun reduceState(previousState: ConfirmChatRoomDeleteState, stateChange: Any): ConfirmChatRoomDeleteState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true, error = null)
      is CloseDialog -> previousState.copy(closeDialog = true)
      is Error -> previousState.copy(loading = false, error = stateChange.error)
      is HideError -> previousState.copy()
      else -> previousState
    }
}