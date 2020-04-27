package com.gis.repository.data.repository

import com.gis.repository.domain.datasource.ChatRoomsDataSource
import com.gis.repository.domain.entity.ChatRoom
import com.gis.repository.domain.repository.ChatRoomsRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class ChatRoomsRepositoryImpl(
  private val localChatRoomsDataSource: ChatRoomsDataSource,
  private val remoteChatRoomsDataSource: ChatRoomsDataSource
) : ChatRoomsRepository {

  private val chatRoomsPublisher = BehaviorSubject.createDefault<List<ChatRoom>>(emptyList())

  init {
    startObserveChatRooms()
      .subscribe()
  }

  private fun startObserveChatRooms(): Observable<List<ChatRoom>> =
    localChatRoomsDataSource.getChatRooms()
      .switchMap { localRooms ->
        if (localRooms.isEmpty())
          remoteChatRoomsDataSource.loadNextPage(1, 20)
            .switchMap { remoteRooms ->
              localChatRoomsDataSource.addChatRooms(remoteRooms)
                .toSingleDefault(remoteRooms)
                .toObservable()
                .doOnNext { chatRoomsPublisher.onNext(remoteRooms) }
            }
        else
          Observable.just(localRooms)
            .doOnNext { chatRoomsPublisher.onNext(localRooms) }
      }

  override fun observeChatRooms(): Observable<List<ChatRoom>> = chatRoomsPublisher

  override fun refreshChatRooms(): Completable =
    localChatRoomsDataSource.clearDataSource()
      .andThen { Completable.fromObservable(startObserveChatRooms()) }

  override fun searchChatRooms(search: String): Observable<List<ChatRoom>> =
    remoteChatRoomsDataSource.searchChatRooms(search)

  override fun loadNextPage(pageNum: Int, perPage: Int): Completable =
    remoteChatRoomsDataSource.loadNextPage(pageNum, perPage)
      .flatMapCompletable { rooms ->
        localChatRoomsDataSource.addChatRooms(rooms)
      }

  override fun addChatRoom(chatRoom: ChatRoom): Completable =
    remoteChatRoomsDataSource.addChatRoom(chatRoom)
      .flatMapCompletable { room -> Completable.fromObservable(localChatRoomsDataSource.addChatRoom(room)) }

  override fun updateChatRoom(chatRoom: ChatRoom): Completable =
    remoteChatRoomsDataSource.updateChatRoom(chatRoom)
      .flatMapCompletable { room -> Completable.fromObservable(localChatRoomsDataSource.updateChatRoom(room)) }

  override fun deleteChatRoom(chatId: Int): Completable =
    remoteChatRoomsDataSource.deleteChatRoom(chatId)
      .andThen(localChatRoomsDataSource.deleteChatRoom(chatId))
}