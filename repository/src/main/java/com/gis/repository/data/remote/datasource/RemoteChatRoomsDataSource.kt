package com.gis.repository.data.remote.datasource

import com.gis.repository.data.remote.api.AppTemplateApi
import com.gis.repository.domain.datasource.ChatRoomsDataSource
import com.gis.repository.domain.entity.ChatRoom
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit

class RemoteChatRoomsDataSource(private val api: AppTemplateApi) : ChatRoomsDataSource {

  private val chatRooms = mutableListOf<ChatRoom>().apply {
    for (i in 1..100) {
      add(
        ChatRoom(
          chatId = i,
          name = "ChatRoom #$i",
          avatarUrl = "https://cdn.fishki.net/upload/post/201507/28/1610020/e5Gm6Kb.jpg",
          lastMessage = "Last message $i",
          lastMessageDate = Date()
        )
      )
    }
  }

  override fun searchChatRooms(search: String): Observable<List<ChatRoom>> {
    if (search.isBlank())
      return Observable.just(emptyList())
    val searchResult = mutableListOf<ChatRoom>()
    for (room in chatRooms) {
      if (room.name.contains(search))
        searchResult.add(room)
    }
    return Observable.just(searchResult)
  }

  override fun getChatRooms(): Observable<List<ChatRoom>> =
    loadNextPage(1, 20)

  override fun loadNextPage(pageNum: Int, perPage: Int): Observable<List<ChatRoom>> {
    val start = if (pageNum == 1) pageNum else ((pageNum - 1) * perPage) + 1
    val end = start + perPage
    return Observable.timer(2000, TimeUnit.MILLISECONDS)
      .map { chatRooms.filterIndexed { i, room -> i + 1 in start until end } }
  }

  override fun addChatRoom(chatRoom: ChatRoom): Observable<ChatRoom> {
    chatRooms.add(chatRoom)
    return Observable.just(chatRoom)
  }

  override fun addChatRooms(chatRooms: List<ChatRoom>): Completable =
    throw UnsupportedOperationException("Remote data source doesn't support this operation")

  override fun updateChatRoom(chatRoom: ChatRoom): Observable<ChatRoom> =
    Observable.just(chatRoom)

  override fun deleteChatRoom(chatId: Int): Completable =
    Completable.fromAction {
      chatRooms.apply {
        removeAll { room -> room.chatId == chatId }
      }
    }

  override fun clearDataSource(): Completable =
    throw UnsupportedOperationException("Remote data source doesn't support this operation")
}