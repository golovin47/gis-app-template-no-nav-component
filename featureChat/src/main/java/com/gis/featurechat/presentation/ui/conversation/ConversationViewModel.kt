package com.gis.featurechat.presentation.ui.conversation

import android.net.Uri
import com.gis.repository.domain.entity.ChatMessage
import com.gis.repository.domain.entity.Gif
import com.gis.repository.domain.interactors.*
import com.gis.utils.presentation.BaseViewModel
import com.gis.featurechat.presentation.ui.conversation.ConversationIntent.*
import com.gis.featurechat.presentation.ui.conversation.ConversationStateChange.*
import com.gis.featurechat.presentation.ui.conversation.GifListItem.*
import com.gis.featurechat.presentation.ui.conversation.MessageListItem.MessageItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ConversationViewModel(
  private var createFileAndGetUri: (() -> Observable<Uri>)?,
  private var showGalleryImagesScreen: (() -> Unit)?,
  private var showExtraPermissionsDialog: (() -> Unit)?,
  private var goToImagePreview: ((Uri) -> Unit)?,
  private val observeUserUseCase: ObserveUserUseCase,
  private val observeConversationUseCase: ObserveConversationUseCase,
  private val getPopularGifsUseCase: GetPopularGifsUseCase,
  private val searchGifsUseCase: SearchGifsUseCase,
  private val sendChatTextMessageUseCase: SendChatTextMessageUseCase,
  private val sendChatPhotoMessageUseCase: SendChatPhotoMessageUseCase,
  private val sendChatFileMessageuseCase: SendChatFileMessageUseCase
) : BaseViewModel<ConversationState>() {

  override fun initState(): ConversationState = ConversationState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(
      listOf(
        observeUserUseCase.execute()
          .map { UserReceived(it) }
          .cast(ConversationStateChange::class.java)
          .onErrorResumeNext { e: Throwable -> handleError(e) }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
      )
    )

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(

        intentStream.ofType(ObserveConversation::class.java)
          .switchMap { event ->
            observeConversationUseCase.execute(event.id)
              .map { ConversationReceived(it) }
              .cast(ConversationStateChange::class.java)
              .startWith(StartLoading)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(ShowExtraPermissionsDialog::class.java)
          .doOnNext { showExtraPermissionsDialog?.invoke() },

        intentStream.ofType(ChooseFromGallery::class.java)
          .doOnNext { showGalleryImagesScreen?.invoke() },

        intentStream.ofType(ImageChosen::class.java)
          .doOnNext { event -> goToImagePreview?.invoke(event.uri) },

        intentStream.ofType(TakePhoto::class.java)
          .switchMap { event ->
            createFileAndGetUri!!.invoke()
              .flatMap { uri -> Observable.just(OpenCamera(uri), Idle) }
              .cast(ConversationStateChange::class.java)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(OpenGifs::class.java)
          .switchMap { event ->
            if (event.loadGifs) {
              getPopularGifsUseCase.execute()
                .map { gifs -> GifsReceived(gifs.map { it.toPresentation() }) }
                .cast(ConversationStateChange::class.java)
                .startWith(GifsOpened)
                .onErrorResumeNext { e: Throwable -> handleError(e) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

            } else {
              Observable.just(GifsOpened)
            }
          },

        intentStream.ofType(CloseGifs::class.java)
          .map { GifsClosed },

        intentStream.ofType(GetPopularGifs::class.java)
          .switchMap { event ->
            getPopularGifsUseCase.execute()
              .map { gifs -> GifsReceived(gifs.map { it.toPresentation() }) }
              .cast(ConversationStateChange::class.java)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(GetNextGifsPage::class.java)
          .switchMap { event ->
            getPopularGifsUseCase.execute(offset = event.offset)
              .map { gifs -> NextGifsPageReceived(gifs.map { it.toPresentation() }) }
              .cast(ConversationStateChange::class.java)
              .startWith(LoadingNextGifsPage)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(SearchGifs::class.java)
          .switchMap { event ->
            if (event.query.isBlank())
              Observable.just(SearchGifsReceived(emptyList()))
            else
              searchGifsUseCase.execute(event.query)
                .map { gifs ->
                  SearchGifsReceived(
                    if (gifs.isNotEmpty()) gifs.map { it.toPresentation() }
                    else listOf(GifSearchEmptyItem))
                }
                .cast(ConversationStateChange::class.java)
                .onErrorResumeNext { e: Throwable -> handleError(e) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(GetNextSearchGifsPage::class.java)
          .switchMap { event ->
            searchGifsUseCase.execute(query = event.query, offset = event.offset)
              .map { gifs -> NextSearchGifsPageReceived(gifs.map { it.toPresentation() }) }
              .cast(ConversationStateChange::class.java)
              .startWith(LoadingNextSearchGifsPage)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(SendTextMessage::class.java)
          .switchMap { event ->
            sendChatTextMessageUseCase.execute(event.chatTextMessage)
              .andThen(Observable.just(MessageSent, Idle))
              .cast(ConversationStateChange::class.java)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(SendPhotoMessage::class.java)
          .switchMap { event ->
            sendChatPhotoMessageUseCase.execute(event.chatPhotoMessage)
              .andThen(Observable.just(MessageSent, Idle))
              .cast(ConversationStateChange::class.java)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(SendFileMessage::class.java)
          .switchMap { event ->
            sendChatFileMessageuseCase.execute(event.chatFileMessage)
              .andThen(Observable.just(MessageSent, Idle))
              .cast(ConversationStateChange::class.java)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          }
      )
    )

  private fun handleError(e: Throwable) = Observable.just(Error(e), HideError)

  private fun ChatMessage.toPresentation() = MessageItem(this)

  private fun Gif.toPresentation() = GifItem(this)

  private fun processNewGifsPage(gifs: List<GifListItem>, newPage: List<GifListItem>): List<GifListItem> =
    gifs.toMutableList().apply {
      if (gifs.contains(GifLoadingItem))
        remove(GifLoadingItem)
      if (newPage.isNotEmpty()) {
        addAll(newPage)
      }
    }

  private fun processLoadinGifsPage(gifs: List<GifListItem>): List<GifListItem> =
    gifs.toMutableList().apply {
      if (!gifs.contains(GifLoadingItem))
        add(GifLoadingItem)
    }

  private fun processNewSearchGifsPage(gifs: List<GifListItem>, newPage: List<GifListItem>): List<GifListItem> =
    gifs.toMutableList().apply {
      if (gifs.contains(GifLoadingItem))
        remove(GifLoadingItem)
      if (newPage.isNotEmpty()) {
        addAll(newPage)
      }
    }

  private fun processLoadingSearchGifsPage(gifs: List<GifListItem>): List<GifListItem> =
    gifs.toMutableList().apply {
      if (!gifs.contains(GifLoadingItem))
        add(GifLoadingItem)
    }

  override fun reduceState(previousState: ConversationState, stateChange: Any): ConversationState =
    when (stateChange) {
      is Idle -> previousState.copy(loading = false, messageSent = false, openCamera = false, error = null)
      is StartLoading -> previousState.copy(loading = true, error = null)
      is MessageSent -> previousState.copy(messageSent = true)
      is UserReceived -> previousState.copy(author = stateChange.user)
      is ConversationReceived -> previousState.copy(
        loading = false,
        name = stateChange.conversation.name,
        avatarUrl = stateChange.conversation.avatarUrl,
        users = stateChange.conversation.users,
        messages = stateChange.conversation.messages.map { it.toPresentation() }.reversed(),
        error = null
      )
      is GifsOpened -> previousState.copy(gifsOpened = true)
      is GifsClosed -> previousState.copy(gifsOpened = false)
      is GifsReceived -> previousState.copy(gifs = stateChange.gifs)
      is LoadingNextGifsPage -> previousState.copy(gifs = processLoadinGifsPage(previousState.gifs))
      is NextGifsPageReceived -> previousState.copy(gifs = processNewGifsPage(previousState.gifs, stateChange.gifs))
      is SearchGifsReceived -> previousState.copy(gifsSearch = stateChange.gifs)
      is LoadingNextSearchGifsPage -> previousState.copy(gifsSearch = processLoadingSearchGifsPage(previousState.gifsSearch))
      is NextSearchGifsPageReceived -> previousState.copy(
        gifsSearch = processNewSearchGifsPage(
          previousState.gifsSearch,
          stateChange.gifs
        )
      )
      is OpenCamera -> previousState.copy(openCamera = true, imageUri = stateChange.uri)
      is Error -> previousState.copy(loading = false, error = stateChange.error)
      is HideError -> previousState.copy(error = null)
      else -> previousState
    }

  override fun onCleared() {
    showGalleryImagesScreen = null
    showExtraPermissionsDialog = null
    goToImagePreview = null
    super.onCleared()
  }
}