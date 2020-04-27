package com.gis.repository.domain.entity

import java.util.*

sealed class ChatMessage {
  abstract val id: Int
  abstract val author: User
  abstract val date: Date
  abstract val loading: Boolean
  abstract val success: Boolean
  abstract val error: Boolean

  data class ChatTextMessage(
    override val id: Int,
    val text: String,
    override val author: User,
    override val date: Date,
    override val loading: Boolean,
    override val success: Boolean,
    override val error: Boolean
  ) : ChatMessage()

  data class ChatPhotoMessage(
    override val id: Int,
    val imageUrl: String,
    override val author: User,
    override val date: Date,
    override val loading: Boolean,
    override val success: Boolean,
    override val error: Boolean
  ) : ChatMessage()

  data class ChatGifMessage(
    override val id: Int,
    val gifUrl: String,
    override val author: User,
    override val date: Date,
    override val loading: Boolean,
    override val success: Boolean,
    override val error: Boolean
  ) : ChatMessage()

  data class ChatFileMessage(
    override val id: Int,
    val fileName: String,
    val fileSize: String,
    override val author: User,
    override val date: Date,
    override val loading: Boolean,
    override val success: Boolean,
    override val error: Boolean
  ) : ChatMessage()
}
