package com.gis.repository.domain.datasource

import com.gis.repository.domain.entity.ChatMessage
import com.gis.repository.domain.entity.ChatMessage.ChatPhotoMessage
import com.gis.repository.domain.entity.ChatMessage.ChatTextMessage
import com.gis.repository.domain.entity.Conversation
import io.reactivex.Completable
import io.reactivex.Observable

interface ConversationDataSource {

  fun getConversation(id: Int): Observable<Conversation>

  fun loadNextPage(pageNum: Int, perPage: Int): Completable

  fun sendTextMessage(chatTextMessage: ChatTextMessage): Completable

  fun sendPhotoMessage(chatPhotoMessage: ChatPhotoMessage): Completable

  fun sendFileMessage(chatFileMessage: ChatMessage.ChatFileMessage): Completable

  fun removeMessage(id: Int): Completable
}