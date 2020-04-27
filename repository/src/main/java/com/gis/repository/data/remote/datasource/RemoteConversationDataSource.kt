package com.gis.repository.data.remote.datasource

import com.gis.repository.data.remote.api.AppTemplateApi
import com.gis.repository.domain.datasource.ConversationDataSource
import com.gis.repository.domain.entity.ChatMessage
import com.gis.repository.domain.entity.ChatMessage.ChatPhotoMessage
import com.gis.repository.domain.entity.ChatMessage.ChatTextMessage
import com.gis.repository.domain.entity.Conversation
import com.gis.repository.domain.entity.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.*

class RemoteConversationDataSource(private val api: AppTemplateApi) : ConversationDataSource {

  private val conversationPublisher = BehaviorSubject.create<Conversation>()

  override fun getConversation(id: Int): Observable<Conversation> {
    val conversation = Conversation(
      id = id,
      name = "ChatRoom #$id",
      avatarUrl = "https://cdn.fishki.net/upload/post/201507/28/1610020/e5Gm6Kb.jpg",
      users = listOf(
        User(
          id = 1,
          email = "mail1@gmail.com",
          username = "username 1",
          phone = "+5559991",
          birthDate = Date(),
          avatarUrl = "https://static01.nyt.com/images/2017/12/03/books/review/03horowitz2/03horowitz2-articleLarge.jpg"
        ),
        User(
          id = 2,
          email = "mail2@gmail.com",
          username = "username 2",
          phone = "+5559992",
          birthDate = Date(),
          avatarUrl = "https://pp.userapi.com/c308831/g43085136/a_67150b4c.jpg?ava=1.jpg"
        )
      ),
      messages = mutableListOf()
    )

    val curConversation = conversationPublisher.value

    return Observable.just(conversation)
      .doOnNext { c -> if (curConversation == null || (curConversation.id != id)) conversationPublisher.onNext(c) }
      .switchMap { conversationPublisher }
  }

  override fun loadNextPage(pageNum: Int, perPage: Int): Completable =
    Completable.complete()

  override fun sendTextMessage(chatTextMessage: ChatTextMessage): Completable =
    Completable.fromAction {
      conversationPublisher.onNext(
        conversationPublisher.value!!.copy(messages = conversationPublisher.value!!.messages + chatTextMessage)
      )
    }

  override fun sendPhotoMessage(chatPhotoMessage: ChatPhotoMessage): Completable =
    Completable.fromAction {
      conversationPublisher.onNext(
        conversationPublisher.value!!.copy(messages = conversationPublisher.value!!.messages + chatPhotoMessage)
      )
    }

  override fun sendFileMessage(chatFileMessage: ChatMessage.ChatFileMessage): Completable =
    Completable.fromAction {
      conversationPublisher.onNext(
        conversationPublisher.value!!.copy(messages = conversationPublisher.value!!.messages + chatFileMessage)
      )
    }

  override fun removeMessage(id: Int): Completable =
    Completable.fromAction {
      conversationPublisher.onNext(
        conversationPublisher.value!!.copy(messages = conversationPublisher.value!!.messages.filter { m -> m.id != id })
      )
    }
}