package com.gis.repository.data.repository

import com.gis.repository.domain.datasource.ConversationDataSource
import com.gis.repository.domain.entity.ChatMessage.*
import com.gis.repository.domain.repository.ConversationRepository
import io.reactivex.Completable

class ConversationRepositoryImpl(private val remoteConversationDataSource: ConversationDataSource) :
  ConversationRepository {

  override fun observeConversation(id: Int) =
    remoteConversationDataSource.getConversation(id)

  override fun loadNextPage(pageNum: Int, perPage: Int): Completable =
    remoteConversationDataSource.loadNextPage(pageNum, perPage)

  override fun sendTextMessage(chatTextMessage: ChatTextMessage): Completable =
    remoteConversationDataSource.sendTextMessage(chatTextMessage)

  override fun sendPhotoMessage(chatPhotoMessage: ChatPhotoMessage): Completable =
    remoteConversationDataSource.sendPhotoMessage(chatPhotoMessage)

  override fun sendFileMessage(chatFileMessage: ChatFileMessage): Completable =
    remoteConversationDataSource.sendFileMessage(chatFileMessage)

  override fun removeMessage(id: Int): Completable =
    remoteConversationDataSource.removeMessage(id)
}