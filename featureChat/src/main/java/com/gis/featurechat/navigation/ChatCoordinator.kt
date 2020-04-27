package com.gis.featurechat.navigation

import android.net.Uri
import androidx.fragment.app.FragmentManager

class ChatCoordinator(private val chatNavigator: ChatNavigator) {

  fun start(fragmentManager: FragmentManager, fragmentContainer: Int) {
    chatNavigator.setFragmentManager(fragmentManager)
    chatNavigator.setFragmentContainer(fragmentContainer)
    chatNavigator.openChatRoomsScreen()
  }

  fun update(fragmentManager: FragmentManager) {
    chatNavigator.setFragmentManager(fragmentManager)
  }

  fun showChatRoomOptionsDialog(chatId: Int) {
    chatNavigator.showChatRoomOptionsDialog(chatId)
  }

  fun showConfirmChatRoomDeleteDialog(chatId: Int) {
    chatNavigator.showConfirmChatRoomDeleteDialog(chatId)
  }

  fun openChatRoomsSearchScreen() {
    chatNavigator.openChatRoomsSearchScreen()
  }

  fun openNewChatRoomScreen() {
    chatNavigator.openNewChatRoomScreen()
  }

  fun openConversationScreen(chatId: Int) {
    chatNavigator.openConversationScreen(chatId)
  }

  fun openGalleryImagesScreen() {
    chatNavigator.openGalleryImagesScreen()
  }

  fun openMessageImagePreviewScreen(uri: Uri) {
    chatNavigator.openMessageImagePreviewScreen(uri)
  }

  fun goBack() {
    chatNavigator.goBack()
  }
}