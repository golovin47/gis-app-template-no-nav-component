package com.gis.repository.domain.entity

import java.util.*

data class ChatRoom(
  val chatId: Int,
  val name: String,
  val avatarUrl: String,
  val lastMessage: String,
  val lastMessageDate: Date
)