package com.gis.repository.domain.repository

import com.gis.repository.domain.entity.ChatMessage.*
import com.gis.repository.domain.entity.Conversation
import io.reactivex.Completable
import io.reactivex.Observable

interface ConversationRepository {

  fun observeConversation(id: Int): Observable<Conversation>

  fun loadNextPage(pageNum: Int, perPage: Int): Completable

  fun sendTextMessage(chatTextMessage: ChatTextMessage): Completable

  fun sendPhotoMessage(chatPhotoMessage: ChatPhotoMessage): Completable

  fun sendFileMessage(chatFileMessage: ChatFileMessage): Completable

  fun removeMessage(id: Int): Completable
}