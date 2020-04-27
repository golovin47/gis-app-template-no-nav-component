package com.gis.featurechat.presentation.ui.messageimagepreview

import com.gis.repository.domain.interactors.ObserveUserUseCase
import com.gis.repository.domain.interactors.SendChatPhotoMessageUseCase
import com.gis.utils.presentation.BaseViewModel
import io.reactivex.Observable
import com.gis.featurechat.presentation.ui.messageimagepreview.MessageImagePreviewIntent.*
import com.gis.featurechat.presentation.ui.messageimagepreview.MessageImagePreviewStateChange.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MessageImagePreviewViewModel(
  private var goBack: (() -> Unit)?,
  private val observeUserUseCase: ObserveUserUseCase,
  private val sendChatPhotoMessageUseCase: SendChatPhotoMessageUseCase
) : BaseViewModel<MessageImagePreviewState>() {

  override fun initState(): MessageImagePreviewState = MessageImagePreviewState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(listOf(
      observeUserUseCase.execute()
        .map { UserReceived(it) }
        .cast(MessageImagePreviewStateChange::class.java)
        .onErrorResumeNext { e: Throwable -> handleError(e) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    ))

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(listOf(

      intentStream.ofType(SendPhotoMessage::class.java)
        .switchMap { event ->
          sendChatPhotoMessageUseCase.execute(event.photoMessage)
            .toSingleDefault(Any())
            .toObservable()
            .doOnNext { goBack?.invoke() }
        },

      intentStream.ofType(Cancel::class.java)
        .doOnNext { goBack?.invoke() }
    ))

  private fun handleError(e: Throwable) = Observable.just(Error(e), HideError)

  override fun reduceState(previousState: MessageImagePreviewState, stateChange: Any): MessageImagePreviewState =
    when (stateChange) {
      is UserReceived -> previousState.copy(user = stateChange.user)
      is Error -> previousState.copy(error = stateChange.error)
      is HideError -> previousState.copy(error = null)
      else -> previousState
    }

  override fun onCleared() {
    goBack = null
    super.onCleared()
  }
}