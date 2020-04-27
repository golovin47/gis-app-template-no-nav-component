package com.gis.featurechat.presentation.ui.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.gis.repository.domain.entity.ChatMessage.*
import com.gis.utils.bytesToKb
import com.gis.utils.domain.ImageLoader
import com.gis.utils.toChatMessageTime
import com.gis.featurechat.R
import com.gis.featurechat.databinding.*
import com.gis.featurechat.presentation.ui.conversation.MessageListItem.MessageItem
import com.gis.featurechat.presentation.ui.conversation.MessageListItem.MessageLoadingItem
import java.util.*

private const val OTHERS_TEXT_MESSAGE = 0x1006
private const val OTHERS_PHOTO_MESSAGE = 0x1005
private const val OTHERS_GIF_MESSAGE = 0x1004
private const val OTHERS_FILE_MESSAGE = 0x1003
private const val OWN_TEXT_MESSAGE = 0x1002
private const val OWN_PHOTO_MESSAGE = 0x1001
private const val OWN_GIF_MESSAGE = 0x1000
private const val OWN_FILE_MESSAGE = 0x999
private const val LOADING_ITEM = 0x998

private const val UPDATE_TEXT = 0x997
private const val UPDATE_IMAGE = 0x996
private const val UPDATE_GIF = 0x995
private const val UPDATE_FILE_NAME = 0x994
private const val UPDATE_FILE_SIZE = 0x993
private const val UPDATE_USER_AVATAR = 0x992
private const val UPDATE_MESSAGE_DATE = 0x991

class MessagesAdapter(private val imageLoader: ImageLoader) :
  ListAdapter<MessageListItem, ViewHolder>(object : DiffUtil.ItemCallback<MessageListItem>() {
    override fun areItemsTheSame(oldItem: MessageListItem, newItem: MessageListItem): Boolean =
      when {
        oldItem is MessageItem &&
            newItem is MessageItem &&
            oldItem.chatMessage.id == newItem.chatMessage.id -> true
        oldItem is MessageLoadingItem && newItem is MessageLoadingItem -> true
        else -> false
      }

    override fun areContentsTheSame(oldItem: MessageListItem, newItem: MessageListItem): Boolean =
      when {
        oldItem is MessageItem && newItem is MessageItem && oldItem.chatMessage == newItem.chatMessage -> true
        oldItem is MessageLoadingItem && newItem is MessageLoadingItem -> true
        else -> false
      }

    override fun getChangePayload(oldItem: MessageListItem, newItem: MessageListItem): Any? =
      if (oldItem is MessageItem && newItem is MessageItem) {
        if (oldItem.chatMessage is ChatTextMessage && newItem.chatMessage is ChatTextMessage)
          when {
            oldItem.chatMessage.text != newItem.chatMessage.text -> UPDATE_TEXT
            oldItem.chatMessage.author != newItem.chatMessage.author -> UPDATE_USER_AVATAR
            else -> UPDATE_MESSAGE_DATE
          }
        else if (oldItem.chatMessage is ChatPhotoMessage && newItem.chatMessage is ChatPhotoMessage)
          when {
            oldItem.chatMessage.imageUrl != newItem.chatMessage.imageUrl -> UPDATE_IMAGE
            oldItem.chatMessage.author != newItem.chatMessage.author -> UPDATE_USER_AVATAR
            else -> UPDATE_MESSAGE_DATE
          }
        else if (oldItem.chatMessage is ChatGifMessage && newItem.chatMessage is ChatGifMessage)
          when {
            oldItem.chatMessage.gifUrl != newItem.chatMessage.gifUrl -> UPDATE_GIF
            oldItem.chatMessage.author != newItem.chatMessage.author -> UPDATE_USER_AVATAR
            else -> UPDATE_MESSAGE_DATE
          }
        else if (oldItem.chatMessage is ChatFileMessage && newItem.chatMessage is ChatFileMessage)
          when {
            oldItem.chatMessage.fileName != newItem.chatMessage.fileName -> UPDATE_FILE_NAME
            oldItem.chatMessage.fileSize != newItem.chatMessage.fileSize -> UPDATE_FILE_SIZE
            oldItem.chatMessage.author != newItem.chatMessage.author -> UPDATE_USER_AVATAR
            else -> UPDATE_MESSAGE_DATE
          }
        else Any()
      } else {
        Any()
      }
  }) {

  override fun onFailedToRecycleView(holder: ViewHolder): Boolean = true

  override fun getItemViewType(position: Int): Int {
    val item = getItem(position)
    return when (item) {
      is MessageItem -> if (item.chatMessage.author.id != 1) {
        when (item.chatMessage) {
          is ChatTextMessage -> OTHERS_TEXT_MESSAGE
          is ChatPhotoMessage -> OTHERS_PHOTO_MESSAGE
          is ChatGifMessage -> OTHERS_GIF_MESSAGE
          is ChatFileMessage -> OTHERS_FILE_MESSAGE
        }
      } else {
        when (item.chatMessage) {
          is ChatTextMessage -> OWN_TEXT_MESSAGE
          is ChatPhotoMessage -> OWN_PHOTO_MESSAGE
          is ChatGifMessage -> OWN_GIF_MESSAGE
          is ChatFileMessage -> OWN_FILE_MESSAGE
        }
      }
      else -> LOADING_ITEM
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    when (viewType) {
      OTHERS_TEXT_MESSAGE -> OthersTextMessageViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_others_chat_text_message,
          parent,
          false
        ),
        imageLoader
      )

      OTHERS_PHOTO_MESSAGE -> OthersPhotoMessageViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_others_chat_photo_message,
          parent,
          false
        ),
        imageLoader
      )

      OTHERS_GIF_MESSAGE -> OthersGifMessageViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_others_chat_gif_message,
          parent,
          false
        ),
        imageLoader
      )

      OTHERS_FILE_MESSAGE -> OthersFileMessageViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_others_chat_file_message,
          parent,
          false
        ),
        imageLoader
      )

      OWN_TEXT_MESSAGE -> OwnTextMessageViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_own_chat_text_message,
          parent,
          false
        ),
        imageLoader
      )

      OWN_PHOTO_MESSAGE -> OwnPhotoMessageViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_own_chat_photo_message,
          parent,
          false
        ),
        imageLoader
      )

      OWN_GIF_MESSAGE -> OwnGifMessageViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_own_chat_gif_message,
          parent,
          false
        ),
        imageLoader
      )

      OWN_FILE_MESSAGE -> OwnFileMessageViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_own_chat_file_message,
          parent,
          false
        ),
        imageLoader
      )

      else -> ConversationLoadingViewHolder(
        DataBindingUtil.inflate(
          LayoutInflater.from(parent.context),
          R.layout.item_conversation_loading,
          parent,
          false
        )
      )
    }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    if (item is MessageItem) {
      when (holder) {
        is OthersTextMessageViewHolder -> {
          holder.bindTextMessage(item.chatMessage as ChatTextMessage, checkIfOthersFirstMessage(item, position))
        }

        is OthersPhotoMessageViewHolder -> {
          holder.bindPhotoMessage(item.chatMessage as ChatPhotoMessage, checkIfOthersFirstMessage(item, position))
        }

        is OthersGifMessageViewHolder -> {
          holder.bindGifMessage(item.chatMessage as ChatGifMessage, checkIfOthersFirstMessage(item, position))
        }

        is OthersFileMessageViewHolder -> {
          holder.bindFileMessage(item.chatMessage as ChatFileMessage, checkIfOthersFirstMessage(item, position))
        }

        is OwnTextMessageViewHolder -> {
          holder.bindTextMessage(item.chatMessage as ChatTextMessage, checkIfOwnFirstMessage(item, position))
        }

        is OwnPhotoMessageViewHolder -> {
          holder.bindPhotoMessage(item.chatMessage as ChatPhotoMessage, checkIfOwnFirstMessage(item, position))
        }

        is OwnGifMessageViewHolder -> {
          holder.bindGifMessage(item.chatMessage as ChatGifMessage, checkIfOwnFirstMessage(item, position))
        }

        is OwnFileMessageViewHolder -> {
          holder.bindFileMessage(item.chatMessage as ChatFileMessage, checkIfOwnFirstMessage(item, position))
        }
      }
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
    val item = getItem(position)

    if (item is MessageItem) {
      when (holder) {

        is OthersTextMessageViewHolder -> {
          if (payloads.isEmpty()) {
            holder.bindTextMessage(item.chatMessage as ChatTextMessage, checkIfOwnFirstMessage(item, position))
          } else {
            for (payload in payloads) {
              when (payload) {
                UPDATE_TEXT -> holder.updateText((item.chatMessage as ChatTextMessage).text)
                UPDATE_USER_AVATAR -> holder.updateUserAvatar(item.chatMessage.author.avatarUrl)
                else -> holder.updateDate(item.chatMessage.date)
              }
            }
          }
        }

        is OthersPhotoMessageViewHolder -> {
          if (payloads.isEmpty()) {
            holder.bindPhotoMessage(item.chatMessage as ChatPhotoMessage, checkIfOthersFirstMessage(item, position))
          } else {
            for (payload in payloads) {
              when (payload) {
                UPDATE_IMAGE -> holder.updateImage((item.chatMessage as ChatPhotoMessage).imageUrl)
                UPDATE_USER_AVATAR -> holder.updateUserAvatar(item.chatMessage.author.avatarUrl)
                else -> holder.updateDate(item.chatMessage.date)
              }
            }
          }
        }

        is OthersGifMessageViewHolder -> {
          if (payloads.isEmpty()) {
            holder.bindGifMessage(item.chatMessage as ChatGifMessage, checkIfOthersFirstMessage(item, position))
          } else {
            for (payload in payloads) {
              when (payload) {
                UPDATE_GIF -> holder.updateGif((item.chatMessage as ChatGifMessage).gifUrl)
                UPDATE_USER_AVATAR -> holder.updateUserAvatar(item.chatMessage.author.avatarUrl)
                else -> holder.updateDate(item.chatMessage.date)
              }
            }
          }
        }

        is OthersFileMessageViewHolder -> {
          if (payloads.isEmpty()) {
            holder.bindFileMessage(item.chatMessage as ChatFileMessage, checkIfOthersFirstMessage(item, position))
          } else {
            for (payload in payloads) {
              when (payload) {
                UPDATE_FILE_NAME -> holder.updateFileName((item.chatMessage as ChatFileMessage).fileName)
                UPDATE_FILE_SIZE -> holder.updateFileSize((item.chatMessage as ChatFileMessage).fileSize)
                UPDATE_USER_AVATAR -> holder.updateUserAvatar(item.chatMessage.author.avatarUrl)
                else -> holder.updateDate(item.chatMessage.date)
              }
            }
          }
        }

        is OwnTextMessageViewHolder -> {
          if (payloads.isEmpty()) {
            holder.bindTextMessage(item.chatMessage as ChatTextMessage, checkIfOwnFirstMessage(item, position))
          } else {
            for (payload in payloads) {
              when (payload) {
                UPDATE_TEXT -> holder.updateText((item.chatMessage as ChatTextMessage).text)
                UPDATE_USER_AVATAR -> holder.updateUserAvatar(item.chatMessage.author.avatarUrl)
                else -> holder.updateDate(item.chatMessage.date)
              }
            }
          }
        }

        is OwnPhotoMessageViewHolder -> {
          if (payloads.isEmpty()) {
            holder.bindPhotoMessage(item.chatMessage as ChatPhotoMessage, checkIfOwnFirstMessage(item, position))
          } else {
            for (payload in payloads) {
              when (payload) {
                UPDATE_IMAGE -> holder.updateImage((item.chatMessage as ChatPhotoMessage).imageUrl)
                UPDATE_USER_AVATAR -> holder.updateUserAvatar(item.chatMessage.author.avatarUrl)
                else -> holder.updateDate(item.chatMessage.date)
              }
            }
          }
        }

        is OwnGifMessageViewHolder -> {
          if (payloads.isEmpty()) {
            holder.bindGifMessage(item.chatMessage as ChatGifMessage, checkIfOwnFirstMessage(item, position))
          } else {
            for (payload in payloads) {
              when (payload) {
                UPDATE_GIF -> holder.updateGif((item.chatMessage as ChatGifMessage).gifUrl)
                UPDATE_USER_AVATAR -> holder.updateUserAvatar(item.chatMessage.author.avatarUrl)
                else -> holder.updateDate(item.chatMessage.date)
              }
            }
          }
        }

        is OwnFileMessageViewHolder -> {
          if (payloads.isEmpty()) {
            holder.bindFileMessage(item.chatMessage as ChatFileMessage, checkIfOwnFirstMessage(item, position))
          } else {
            for (payload in payloads) {
              when (payload) {
                UPDATE_FILE_NAME -> holder.updateFileName((item.chatMessage as ChatFileMessage).fileName)
                UPDATE_FILE_SIZE -> holder.updateFileSize((item.chatMessage as ChatFileMessage).fileSize)
                UPDATE_USER_AVATAR -> holder.updateUserAvatar(item.chatMessage.author.avatarUrl)
                else -> holder.updateDate(item.chatMessage.date)
              }
            }
          }
        }
      }
    }
  }

  private fun checkIfOwnFirstMessage(item: MessageItem, position: Int): Boolean =
    when {
      itemCount == 1 && position == 0 -> item.chatMessage.author.id == 1
      itemCount > 1 && itemCount - 1 == position -> item.chatMessage.author.id == 1
      itemCount > 1 && position == 0 -> (getItem(position + 1) as MessageItem).chatMessage.author.id != 1
      itemCount > 1 && position > 0 && itemCount - 1 > position ->
        (getItem(position + 1) as MessageItem).chatMessage.author.id != 1 &&
            (getItem(position - 1) as MessageItem).chatMessage.author.id == 1
      else -> false
    }

  private fun checkIfOthersFirstMessage(item: MessageItem, position: Int): Boolean =
    when {
      itemCount == 1 -> position == 0 && item.chatMessage.author.id != 1
      itemCount > 1 && position == 0 -> (getItem(position + 1) as MessageItem).chatMessage.author.id == 1
      itemCount > 1 && position > 0 && itemCount - 1 > position ->
        (getItem(position + 1) as MessageItem).chatMessage.author.id == 1 &&
            (getItem(position - 1) as MessageItem).chatMessage.author.id != 1
      itemCount > 1 && itemCount - 1 == position ->
        (getItem(position - 1) as MessageItem).chatMessage.author.id != 1
      else -> false
    }
}


class OthersTextMessageViewHolder(
  private val binding: ItemOthersChatTextMessageBinding,
  private val imageLoader: ImageLoader
) : ViewHolder(binding.root) {

  fun bindTextMessage(textMessage: ChatTextMessage, firstMessage: Boolean) {
    with(binding) {
      bgView.setBackgroundResource(if (firstMessage) R.drawable.shape_own_message else R.drawable.shape_message)

      tvTextMessage.text = textMessage.text

      if (!textMessage.author.avatarUrl.isBlank()) {
        ivAvatar.visibility = View.VISIBLE
        imageLoader.loadImg(ivAvatar, textMessage.author.avatarUrl, true)
      } else {
        ivAvatar.visibility = View.INVISIBLE
      }

      messageDate.text = textMessage.date.toChatMessageTime()
    }
  }

  fun updateText(newText: String) {
    binding.tvTextMessage.text = newText
  }

  fun updateUserAvatar(userAvatarUrl: String) {
    imageLoader.loadImg(binding.ivAvatar, userAvatarUrl, true)
  }

  fun updateDate(newDate: Date) {
    binding.messageDate.text = newDate.toChatMessageTime()
  }
}


class OthersPhotoMessageViewHolder(
  private val binding: ItemOthersChatPhotoMessageBinding,
  private val imageLoader: ImageLoader
) : ViewHolder(binding.root) {

  fun bindPhotoMessage(photoMessage: ChatPhotoMessage, firstMessage: Boolean) {
    with(binding) {
      imageLoader.loadImg(ivImageMessage, photoMessage.imageUrl, false)

      bgView.setBackgroundResource(if (firstMessage) R.drawable.shape_own_message else R.drawable.shape_message)

      if (!photoMessage.author.avatarUrl.isBlank()) {
        ivAvatar.visibility = View.VISIBLE
        imageLoader.loadImg(ivAvatar, photoMessage.author.avatarUrl, true)
      } else {
        ivAvatar.visibility = View.INVISIBLE
      }

      messageDate.text = photoMessage.date.toChatMessageTime()
    }
  }

  fun updateImage(avatarUrl: String) {
    imageLoader.loadImg(binding.ivImageMessage, avatarUrl, false)
  }

  fun updateUserAvatar(userAvatarUrl: String) {
    imageLoader.loadImg(binding.ivAvatar, userAvatarUrl, true)
  }

  fun updateDate(newDate: Date) {
    binding.messageDate.text = newDate.toChatMessageTime()
  }
}


class OthersGifMessageViewHolder(
  private val binding: ItemOthersChatGifMessageBinding,
  private val imageLoader: ImageLoader
) : ViewHolder(binding.root) {

  fun bindGifMessage(gifMessage: ChatGifMessage, firstMessage: Boolean) {
    with(binding) {
      imageLoader.loadGif(ivGif, gifMessage.gifUrl, false)

      bgView.setBackgroundResource(if (firstMessage) R.drawable.shape_own_message else R.drawable.shape_message)

      if (!gifMessage.author.avatarUrl.isBlank()) {
        ivAvatar.visibility = View.VISIBLE
        imageLoader.loadImg(ivAvatar, gifMessage.author.avatarUrl, true)
      } else {
        ivAvatar.visibility = View.INVISIBLE
      }

      messageDate.text = gifMessage.date.toChatMessageTime()
    }
  }

  fun updateGif(gifUrl: String) {
    imageLoader.loadGif(binding.ivGif, gifUrl, false)
  }

  fun updateUserAvatar(userAvatarUrl: String) {
    imageLoader.loadImg(binding.ivAvatar, userAvatarUrl, true)
  }

  fun updateDate(newDate: Date) {
    binding.messageDate.text = newDate.toChatMessageTime()
  }
}


class OthersFileMessageViewHolder(
  private val binding: ItemOthersChatFileMessageBinding,
  private val imageLoader: ImageLoader
) : ViewHolder(binding.root) {

  fun bindFileMessage(fileMessage: ChatFileMessage, firstMessage: Boolean) {
    with(binding) {

      tvFileName.text = fileMessage.fileName
      tvFileSize.text =
        binding.root.context.getString(R.string.file_size, fileMessage.fileSize.toLong().bytesToKb().toString())

      bgView.setBackgroundResource(if (firstMessage) R.drawable.shape_own_message else R.drawable.shape_message)

      if (!fileMessage.author.avatarUrl.isBlank()) {
        ivAvatar.visibility = View.VISIBLE
        imageLoader.loadImg(ivAvatar, fileMessage.author.avatarUrl, true)
      } else {
        ivAvatar.visibility = View.INVISIBLE
      }

      messageDate.text = fileMessage.date.toChatMessageTime()
    }
  }

  fun updateFileName(fileName: String) {
    binding.tvFileName.text = fileName
  }

  fun updateFileSize(fileSize: String) {
    binding.tvFileSize.text =
      binding.root.context.getString(R.string.file_size, fileSize.toLong().bytesToKb().toString())
  }

  fun updateUserAvatar(userAvatarUrl: String) {
    imageLoader.loadImg(binding.ivAvatar, userAvatarUrl, true)
  }

  fun updateDate(newDate: Date) {
    binding.messageDate.text = newDate.toChatMessageTime()
  }
}

class OwnTextMessageViewHolder(
  private val binding: ItemOwnChatTextMessageBinding,
  private val imageLoader: ImageLoader
) : ViewHolder(binding.root) {

  fun bindTextMessage(textMessage: ChatTextMessage, firstMessage: Boolean) {
    with(binding) {
      bgView.setBackgroundResource(if (firstMessage) R.drawable.shape_own_message else R.drawable.shape_message)

      tvTextMessage.text = textMessage.text

      if (!textMessage.author.avatarUrl.isBlank()) {
        ivAvatar.visibility = View.VISIBLE
        imageLoader.loadImg(ivAvatar, textMessage.author.avatarUrl, true)
      } else {
        ivAvatar.visibility = View.INVISIBLE
      }

      messageDate.text = textMessage.date.toChatMessageTime()
    }
  }

  fun updateText(newText: String) {
    binding.tvTextMessage.text = newText
  }

  fun updateUserAvatar(userAvatarUrl: String) {
    imageLoader.loadImg(binding.ivAvatar, userAvatarUrl, true)
  }

  fun updateDate(newDate: Date) {
    binding.messageDate.text = newDate.toChatMessageTime()
  }
}


class OwnPhotoMessageViewHolder(
  private val binding: ItemOwnChatPhotoMessageBinding,
  private val imageLoader: ImageLoader
) : ViewHolder(binding.root) {

  fun bindPhotoMessage(photoMessage: ChatPhotoMessage, firstMessage: Boolean) {
    with(binding) {
      imageLoader.loadImg(ivImageMessage, photoMessage.imageUrl, false)

      bgView.setBackgroundResource(if (firstMessage) R.drawable.shape_own_message else R.drawable.shape_message)

      if (!photoMessage.author.avatarUrl.isBlank()) {
        ivAvatar.visibility = View.VISIBLE
        imageLoader.loadImg(ivAvatar, photoMessage.author.avatarUrl, true)
      } else {
        ivAvatar.visibility = View.INVISIBLE
      }

      messageDate.text = photoMessage.date.toChatMessageTime()
    }
  }

  fun updateImage(avatarUrl: String) {
    imageLoader.loadImg(binding.ivImageMessage, avatarUrl, false)
  }

  fun updateUserAvatar(userAvatarUrl: String) {
    imageLoader.loadImg(binding.ivAvatar, userAvatarUrl, true)
  }

  fun updateDate(newDate: Date) {
    binding.messageDate.text = newDate.toChatMessageTime()
  }
}


class OwnGifMessageViewHolder(
  private val binding: ItemOwnChatGifMessageBinding,
  private val imageLoader: ImageLoader
) : ViewHolder(binding.root) {

  fun bindGifMessage(gifMessage: ChatGifMessage, firstMessage: Boolean) {
    with(binding) {
      imageLoader.loadGif(ivGif, gifMessage.gifUrl, false)

      bgView.setBackgroundResource(if (firstMessage) R.drawable.shape_own_message else R.drawable.shape_message)

      if (!gifMessage.author.avatarUrl.isBlank()) {
        ivAvatar.visibility = View.VISIBLE
        imageLoader.loadImg(ivAvatar, gifMessage.author.avatarUrl, true)
      } else {
        ivAvatar.visibility = View.INVISIBLE
      }

      messageDate.text = gifMessage.date.toChatMessageTime()
    }
  }

  fun updateGif(gifUrl: String) {
    imageLoader.loadGif(binding.ivGif, gifUrl, false)
  }

  fun updateUserAvatar(userAvatarUrl: String) {
    imageLoader.loadImg(binding.ivAvatar, userAvatarUrl, true)
  }

  fun updateDate(newDate: Date) {
    binding.messageDate.text = newDate.toChatMessageTime()
  }
}


class OwnFileMessageViewHolder(
  private val binding: ItemOwnChatFileMessageBinding,
  private val imageLoader: ImageLoader
) : ViewHolder(binding.root) {

  fun bindFileMessage(fileMessage: ChatFileMessage, firstMessage: Boolean) {
    with(binding) {

      tvFileName.text = fileMessage.fileName
      tvFileSize.text =
        binding.root.context.getString(R.string.file_size, fileMessage.fileSize.toLong().bytesToKb().toString())

      bgView.setBackgroundResource(if (firstMessage) R.drawable.shape_own_message else R.drawable.shape_message)

      if (!fileMessage.author.avatarUrl.isBlank()) {
        ivAvatar.visibility = View.VISIBLE
        imageLoader.loadImg(ivAvatar, fileMessage.author.avatarUrl, true)
      } else {
        ivAvatar.visibility = View.INVISIBLE
      }

      messageDate.text = fileMessage.date.toChatMessageTime()
    }
  }

  fun updateFileName(fileName: String) {
    binding.tvFileName.text = fileName
  }

  fun updateFileSize(fileSize: String) {
    binding.tvFileSize.text =
      binding.root.context.getString(R.string.file_size, fileSize.toLong().bytesToKb().toString())
  }

  fun updateUserAvatar(userAvatarUrl: String) {
    imageLoader.loadImg(binding.ivAvatar, userAvatarUrl, true)
  }

  fun updateDate(newDate: Date) {
    binding.messageDate.text = newDate.toChatMessageTime()
  }
}


class ConversationLoadingViewHolder(binding: ItemConversationLoadingBinding) : RecyclerView.ViewHolder(binding.root)