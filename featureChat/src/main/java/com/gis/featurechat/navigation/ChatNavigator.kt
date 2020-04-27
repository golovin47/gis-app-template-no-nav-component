package com.gis.featurechat.navigation

import android.net.Uri
import androidx.fragment.app.FragmentManager
import com.gis.featurechat.R
import com.gis.featurechat.presentation.ui.chatroomoptions.ChatRoomOptionsDialogFragment
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsFragment
import com.gis.featurechat.presentation.ui.conversation.ConversationFragment
import com.gis.featurechat.presentation.ui.deletechatroom.ConfirmChatRoomDeleteDialogFragment
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImagesFragment
import com.gis.featurechat.presentation.ui.messageimagepreview.MessageImagePreviewFragment

class ChatNavigator {

  private var fragmentManager: FragmentManager? = null
  private var fragmentContainer: Int = -1

  fun setFragmentManager(fragmentManager: FragmentManager) {
    this.fragmentManager = fragmentManager
  }

  fun setFragmentContainer(fragmentContainer: Int) {
    this.fragmentContainer = fragmentContainer
  }

  fun openChatRoomsScreen() {
    fragmentManager?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, ChatRoomsFragment())
      ?.addToBackStack("ChatRoomsFragment")
      ?.commit()
  }

  fun showChatRoomOptionsDialog(chatId: Int) {
    ChatRoomOptionsDialogFragment.getInstance(chatId).show(fragmentManager!!, "ChatRoomOptionsDialogFragment")
  }

  fun showConfirmChatRoomDeleteDialog(chatId: Int) {
    ConfirmChatRoomDeleteDialogFragment.getInstance(chatId).show(fragmentManager!!, "ConfirmChatRoomDeleteDialogFragment")
  }

  fun openChatRoomsSearchScreen() {

  }

  fun openNewChatRoomScreen() {

  }

  fun openConversationScreen(chatId: Int) {
    fragmentManager?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, ConversationFragment.getInstance(chatId))
      ?.addToBackStack("ConversationFragment")
      ?.commit()
  }

  fun openGalleryImagesScreen() {
    fragmentManager?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, GalleryImagesFragment())
      ?.addToBackStack("GalleryImagesFragment")
      ?.commit()
  }

  fun openMessageImagePreviewScreen(uri: Uri) {
    fragmentManager?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, MessageImagePreviewFragment.getInstance(uri))
      ?.addToBackStack("MessageImagePreviewFragment")
      ?.commit()
  }

  fun goBack() {
    fragmentManager?.popBackStackImmediate()
  }
}