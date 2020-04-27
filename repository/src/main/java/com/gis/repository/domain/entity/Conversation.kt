package com.gis.repository.domain.entity

data class Conversation(
  val id: Int,
  val name: String,
  val avatarUrl: String,
  val users: List<User>,
  val messages: List<ChatMessage>
)