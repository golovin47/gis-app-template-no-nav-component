package com.gis.featurechat.presentation.ui.chatrooms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gis.repository.domain.entity.ChatRoom
import com.gis.utils.databinding.EmptySearchPlaceholderBinding
import com.gis.utils.domain.ImageLoader
import com.gis.utils.toDayMonthYearString
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featurechat.R
import com.gis.featurechat.databinding.ItemChatRoomBinding
import com.gis.featurechat.databinding.ItemChatRoomLoadingBinding
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsIntent.GoToChatRoom
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsIntent.LoadNextChatRoomsPage
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsIntent.ShowChatRoomOptionsDialog
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsListItem.*
import io.reactivex.subjects.Subject
import java.util.*
import java.util.concurrent.TimeUnit

private const val UPDATE_NAME = 0x999
private const val UPDATE_AVATAR = 0x998
private const val UPDATE_LAST_MESSAGE = 0x997
private const val UPDATE_LAST_MESSAGE_DATE = 0x996

private const val DEFAULT_ITEM = 0x995
private const val LOADING_ITEM = 0x994
private const val EMPTY_SEARCH_ITEM = 0x993

class ChatRoomsAdapter(
  private val intentsPublisher: Subject<ChatRoomsIntent>,
  private val imageLoader: ImageLoader
) :
  ListAdapter<ChatRoomsListItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<ChatRoomsListItem>() {

    override fun areItemsTheSame(oldItem: ChatRoomsListItem, newItem: ChatRoomsListItem): Boolean =
      when {
        oldItem is ChatRoomsItem && newItem is ChatRoomsItem -> oldItem.chatRoom.chatId == newItem.chatRoom.chatId
        oldItem is ChatRoomsLoadingItem && newItem is ChatRoomsLoadingItem -> true
        oldItem is ChatRoomsEmptySearchItem && newItem is ChatRoomsEmptySearchItem -> true
        else -> false
      }

    override fun areContentsTheSame(oldItem: ChatRoomsListItem, newItem: ChatRoomsListItem): Boolean =
      oldItem == newItem

    override fun getChangePayload(oldItem: ChatRoomsListItem, newItem: ChatRoomsListItem): Any =
      if (oldItem is ChatRoomsItem && newItem is ChatRoomsItem) {
        when {
          oldItem.chatRoom.name != newItem.chatRoom.name -> UPDATE_NAME
          oldItem.chatRoom.lastMessage != newItem.chatRoom.lastMessage -> UPDATE_LAST_MESSAGE
          oldItem.chatRoom.lastMessageDate != newItem.chatRoom.lastMessageDate -> UPDATE_LAST_MESSAGE_DATE
          else -> UPDATE_AVATAR
        }
      } else Any()
  }) {

  private val perPage: Int = 20

  override fun getItemViewType(position: Int): Int {
    val item = getItem(position)
    return when (item) {
      is ChatRoomsLoadingItem -> LOADING_ITEM
      is ChatRoomsEmptySearchItem -> EMPTY_SEARCH_ITEM
      else -> DEFAULT_ITEM
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
    when (viewType) {
      LOADING_ITEM -> ChatRoomLoadingViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_chat_room_loading,
          parent,
          false
        )
      )

      EMPTY_SEARCH_ITEM -> ChatRoomEmptySearchViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.empty_search_placeholder,
          parent,
          false
        )
      )

      else -> ChatRoomViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_chat_room,
          parent,
          false
        ),
        intentsPublisher,
        imageLoader
      )
    }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder is ChatRoomViewHolder) {
      val chatRoom = getItem(position) as ChatRoomsItem
      holder.bindChatRoom(chatRoom.chatRoom)
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
    loadNextPageIfNeed(position)

    val chatRoom = getItem(position)
    if (chatRoom is ChatRoomsItem && holder is ChatRoomViewHolder) {
      if (payloads.isEmpty())
        holder.bindChatRoom(chatRoom.chatRoom)
      else {
        for (payload in payloads) {
          when (payload) {
            UPDATE_NAME -> holder.updateName(chatRoom.chatRoom.name)
            UPDATE_LAST_MESSAGE -> holder.updateLastMessage(chatRoom.chatRoom.lastMessage)
            UPDATE_LAST_MESSAGE_DATE -> holder.updateLastMessageDate(chatRoom.chatRoom.lastMessageDate)
            else -> holder.updateAvatar(chatRoom.chatRoom.avatarUrl)
          }
        }
      }
    }
  }


  private fun loadNextPageIfNeed(position: Int) {
    if (itemCount > 0 &&
      itemCount % perPage == 0 &&
      position == itemCount - 1 &&
      getItem(itemCount - 1) != ChatRoomsLoadingItem
    ) {
      val nextPage = (itemCount / perPage) + 1
      intentsPublisher.onNext(LoadNextChatRoomsPage(nextPage, perPage))
    }
  }
}


class ChatRoomViewHolder(
  private val binding: ItemChatRoomBinding,
  private val intentsPublisher: Subject<ChatRoomsIntent>,
  private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(binding.root) {

  fun bindChatRoom(chatRoom: ChatRoom) {
    with(binding) {
      tvChatRoomName.text = chatRoom.name
      tvLastMessage.text = chatRoom.lastMessage
      tvLastMessageDate.text = chatRoom.lastMessageDate.toDayMonthYearString()
      imageLoader.loadImg(ivChatRoomAvatar, chatRoom.avatarUrl, true)

      RxView.clicks(itemRoot)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { GoToChatRoom(chatRoom.chatId) }
        .subscribe(intentsPublisher)

      RxView.longClicks(itemRoot)
        .map { ShowChatRoomOptionsDialog(chatRoom.chatId) }
        .subscribe(intentsPublisher)
    }
  }

  fun updateName(name: String) {
    binding.tvChatRoomName.text = name
  }

  fun updateAvatar(url: String) {
    imageLoader.loadImg(binding.ivChatRoomAvatar, url, true)
  }

  fun updateLastMessage(lastMessage: String) {
    binding.tvLastMessage.text = lastMessage
  }

  fun updateLastMessageDate(lastMessageDate: Date) {
    binding.tvLastMessageDate.text = lastMessageDate.toDayMonthYearString()
  }
}

class ChatRoomLoadingViewHolder(binding: ItemChatRoomLoadingBinding) : RecyclerView.ViewHolder(binding.root)

class ChatRoomEmptySearchViewHolder(binding: EmptySearchPlaceholderBinding) : RecyclerView.ViewHolder(binding.root)