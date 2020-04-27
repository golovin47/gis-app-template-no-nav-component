package com.gis.repository.domain.datasource

import com.gis.repository.domain.entity.ChatRoom
import io.reactivex.Completable
import io.reactivex.Observable

interface ChatRoomsDataSource {

  fun getChatRooms(): Observable<List<ChatRoom>>

  fun searchChatRooms(search: String): Observable<List<ChatRoom>>

  fun loadNextPage(pageNum: Int, perPage: Int): Observable<List<ChatRoom>>

  fun addChatRoom(chatRoom: ChatRoom): Observable<ChatRoom>

  fun addChatRooms(chatRooms: List<ChatRoom>): Completable

  fun updateChatRoom(chatRoom: ChatRoom): Observable<ChatRoom>

  fun deleteChatRoom(chatId: Int): Completable

  fun clearDataSource(): Completable
}