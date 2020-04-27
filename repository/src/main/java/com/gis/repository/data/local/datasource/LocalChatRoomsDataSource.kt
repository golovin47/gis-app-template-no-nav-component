package com.gis.repository.data.local.datasource

import com.gis.repository.domain.datasource.ChatRoomsDataSource
import com.gis.repository.domain.entity.ChatRoom
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class LocalChatRoomsDataSource : ChatRoomsDataSource {

  private val localChatRooms = BehaviorSubject.createDefault<List<ChatRoom>>(emptyList())

  override fun getChatRooms(): Observable<List<ChatRoom>> = localChatRooms

  override fun searchChatRooms(search: String): Observable<List<ChatRoom>> =
    throw UnsupportedOperationException("Local data source doesn't support this operation")

  override fun loadNextPage(pageNum: Int, perPage: Int): Observable<List<ChatRoom>> =
    throw UnsupportedOperationException("Local data source doesn't support this operation")

  override fun addChatRoom(chatRoom: ChatRoom): Observable<ChatRoom> {
    val curList = localChatRooms.value
    val newList = mutableListOf<ChatRoom>()
    if (curList != null) {
      newList.addAll(curList)
    }
    newList.add(chatRoom)
    localChatRooms.onNext(newList)
    return Observable.just(chatRoom)
  }

  override fun addChatRooms(chatRooms: List<ChatRoom>): Completable =
    Completable.fromAction {
      //TODO Replace with real DB subscription.
      //TODO But this code may be used in cases where there is no need for DB (put it in repository).
      localChatRooms.onNext(
        if (localChatRooms.hasValue()) {
          val oldList = localChatRooms.value!!.toMutableList()
          val newPage = chatRooms.toMutableList()
          for ((i, item) in oldList.withIndex()) {
            chatRooms.find { room -> room.chatId == item.chatId }?.also {
              oldList[i] = it
              newPage.remove(it)
            }
          }
          oldList + newPage
        } else
          chatRooms
      )
    }

  override fun updateChatRoom(chatRoom: ChatRoom): Observable<ChatRoom> {
    val newList =
      localChatRooms.value!!.toMutableList().apply {
        map { localRoom -> if (localRoom.chatId == chatRoom.chatId) chatRoom else localRoom }
      }
    localChatRooms.onNext(newList)
    return Observable.just(chatRoom)
  }

  override fun deleteChatRoom(chatId: Int): Completable =
    Completable.fromAction {
      val newList =
        localChatRooms.value!!.toMutableList().apply {
          val indexToDelete = indexOfFirst { localRoom -> localRoom.chatId == chatId }
          removeAt(indexToDelete)

        }
      localChatRooms.onNext(newList)
    }

  override fun clearDataSource(): Completable =
    Completable.fromAction { localChatRooms.onNext(emptyList()) }
}