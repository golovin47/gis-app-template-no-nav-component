package com.gis.featurechat.presentation.ui.chatrooms

import com.gis.repository.domain.entity.ChatRoom
import com.gis.repository.domain.interactors.LoadNextChatRoomsPageUseCase
import com.gis.repository.domain.interactors.ObserveChatRoomsUseCase
import com.gis.repository.domain.interactors.RefreshChatRoomsUseCase
import com.gis.repository.domain.interactors.SearchChatRoomsUseCase
import com.gis.utils.presentation.BaseViewModel
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsIntent.*
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsListItem.*
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsStateChange.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ChatRoomsViewModel(
  private var showChatRoomOptionsDialog: ((Int) -> Unit)?,
  private var openConversationScreen: ((Int) -> Unit)?,
  private val observeChatRoomsUseCase: ObserveChatRoomsUseCase,
  private val searchChatRoomsUseCase: SearchChatRoomsUseCase,
  private val refreshChatRoomsUseCase: RefreshChatRoomsUseCase,
  private val loadNextChatRoomsPageUseCase: LoadNextChatRoomsPageUseCase
) : BaseViewModel<ChatRoomsState>() {

  override fun initState(): ChatRoomsState = ChatRoomsState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> = Observable.merge(
    listOf(
      intentStream.ofType(StartObserveChatRooms::class.java)
        .switchMap { event ->
          observeChatRoomsUseCase.execute()
            .map { rooms -> ChatRoomsReceived(rooms.map { it.toPresentation() }) }
            .cast(ChatRoomsStateChange::class.java)
            .startWith(StartLoading)
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        },

      intentStream.ofType(ShowSearchField::class.java)
        .map { SearchFieldShown },

      intentStream.ofType(CloseSearchField::class.java)
        .map { SearchFieldClosed },

      intentStream.ofType(SearchChatRooms::class.java)
        .switchMap { event ->
          if (event.search.isBlank())
            Observable.just(SearchResultReceived(emptyList<ChatRoomsItem>()))
          else
            searchChatRoomsUseCase.execute(event.search)
              .map { rooms ->
                SearchResultReceived(
                  if (rooms.isEmpty()) listOf(ChatRoomsEmptySearchItem) else rooms.map { it.toPresentation() })
              }
              .cast(ChatRoomsStateChange::class.java)
              .startWith(StartLoading)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
        },

      intentStream.ofType(RefreshChatRooms::class.java)
        .switchMap { event ->
          refreshChatRoomsUseCase.execute()
            .toSingleDefault(RefreshFinished)
            .toObservable()
            .cast(ChatRoomsStateChange::class.java)
            .startWith(StartLoading)
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        },

      intentStream.ofType(LoadNextChatRoomsPage::class.java)
        .switchMap { event ->
          loadNextChatRoomsPageUseCase.execute(event.pageNum, event.perPage)
            .toSingleDefault(FinishLoadingNextPage)
            .toObservable()
            .cast(ChatRoomsStateChange::class.java)
            .startWith(StartLoadingNextPage)
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
        },

      intentStream.ofType(ShowChatRoomOptionsDialog::class.java)
        .doOnNext { event -> showChatRoomOptionsDialog?.invoke(event.chatId) },

      intentStream.ofType(GoToChatRoom::class.java)
        .doOnNext { event -> openConversationScreen?.invoke(event.chatId) },

      intentStream.ofType(GoToCreateNewRoom::class.java)
        .doOnNext { }
    )
  )

  private fun handleError(e: Throwable) =
    Observable.just(Error(e), HideError)

  private fun addLoadingItemToRoomsList(rooms: List<ChatRoomsListItem>): List<ChatRoomsListItem> =
    if (!rooms.contains(ChatRoomsLoadingItem))
      rooms.toMutableList().apply {
        add(ChatRoomsLoadingItem)
      }
    else rooms

  private fun removeLoadingItemFromRoomsList(rooms: List<ChatRoomsListItem>): List<ChatRoomsListItem> =
    if (rooms.contains(ChatRoomsLoadingItem))
      rooms.toMutableList().apply {
        remove(ChatRoomsLoadingItem)
      }
    else rooms

  private fun ChatRoom.toPresentation(): ChatRoomsItem =
    ChatRoomsItem(this)

  override fun reduceState(previousState: ChatRoomsState, stateChange: Any): ChatRoomsState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true, error = null)
      is FinishLoading -> previousState.copy(loading = false)
      is RefreshFinished -> previousState.copy(loading = false)
      is SearchFieldShown -> previousState.copy(showSearchField = true)
      is SearchFieldClosed -> previousState.copy(showSearchField = false)
      is StartLoadingNextPage -> previousState.copy(
        chatRooms = addLoadingItemToRoomsList(previousState.chatRooms),
        error = null
      )
      is FinishLoadingNextPage -> previousState.copy(chatRooms = removeLoadingItemFromRoomsList(previousState.chatRooms))
      is ChatRoomsReceived -> previousState.copy(chatRooms = stateChange.chatRooms, loading = false, error = null)
      is SearchResultReceived -> previousState.copy(
        searchResult = stateChange.chatRooms,
        loading = false,
        error = null
      )
      is Error -> previousState.copy(loading = false, error = stateChange.error)
      is HideError -> previousState.copy(error = null)
      else -> previousState
    }

  override fun onCleared() {
    showChatRoomOptionsDialog = null
    openConversationScreen = null
    super.onCleared()
  }
}