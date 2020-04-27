package com.gis.repository.domain.repository

import com.gis.repository.domain.entity.ChatRoom
import io.reactivex.Completable
import io.reactivex.Observable

interface ChatRoomsRepository {

  fun observeChatRooms(): Observable<List<ChatRoom>>

  fun refreshChatRooms(): Completable

  fun searchChatRooms(search: String): Observable<List<ChatRoom>>

  fun loadNextPage(pageNum: Int, perPage: Int): Completable

  fun addChatRoom(chatRoom: ChatRoom): Completable

  fun updateChatRoom(chatRoom: ChatRoom): Completable

  fun deleteChatRoom(chatId: Int): Completable
}