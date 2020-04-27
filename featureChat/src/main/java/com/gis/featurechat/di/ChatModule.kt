package com.gis.featurechat.di

import android.net.Uri
import androidx.fragment.app.FragmentManager
import com.gis.featurechat.navigation.ChatCoordinator
import com.gis.featurechat.navigation.ChatNavigator
import com.gis.featurechat.presentation.ui.chatroomoptions.ChatRoomOptionsViewModel
import com.gis.featurechat.presentation.ui.chatrooms.ChatRoomsViewModel
import com.gis.featurechat.presentation.ui.conversation.ConversationViewModel
import com.gis.featurechat.presentation.ui.deletechatroom.ConfirmChatRoomDeleteViewModel
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImagesViewModel
import com.gis.featurechat.presentation.ui.messageimagepreview.MessageImagePreviewViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val chatModule = module {

  single { ChatCoordinator(get()) }
  single { ChatNavigator() }

  factory(named("showChatRoomsScreen")) {
    { fm: FragmentManager, container: Int -> get<ChatCoordinator>().start(fm, container) }
  }

  factory(named("updateChatRoomsScreen")) {
    { fm: FragmentManager -> get<ChatCoordinator>().update(fm) }
  }

  factory(named("showChatRoomOptionsDialog")) {
    { chatId: Int -> get<ChatCoordinator>().showChatRoomOptionsDialog(chatId) }
  }

  factory(named("showConfirmChatRoomDeleteDialog")) {
    { chatId: Int -> get<ChatCoordinator>().showConfirmChatRoomDeleteDialog(chatId) }
  }

  factory(named("showConversationScreen")) {
    { chatId: Int -> get<ChatCoordinator>().openConversationScreen(chatId) }
  }

  factory(named("showGalleryImagesScreen")) {
    { get<ChatCoordinator>().openGalleryImagesScreen() }
  }

  factory(named("openMessageImagePreviewScreen")) {
    { uri: Uri -> get<ChatCoordinator>().openMessageImagePreviewScreen(uri) }
  }

  factory(named("goBackInChatModule")) {
    { get<ChatCoordinator>().goBack() }
  }

  viewModel {
    ChatRoomsViewModel(
      get(named("showChatRoomOptionsDialog")),
      get(named("showConversationScreen")),
      get(), get(), get(), get()
    )
  }
  viewModel { ChatRoomOptionsViewModel(get(named("showConfirmChatRoomDeleteDialog"))) }
  viewModel { ConfirmChatRoomDeleteViewModel(get()) }
  viewModel {
    ConversationViewModel(
      get(named("createTempFileForPhotoAndGetUri")),
      get(named("showGalleryImagesScreen")),
      get(named("showExtraPermissionsDialog")),
      get(named("openMessageImagePreviewScreen")),
      get(),
      get(),
      get(),
      get(),
      get(),
      get(),
      get()
    )
  }
  viewModel { GalleryImagesViewModel(get(named("goBackInChatModule")), get(), get(), get()) }
  viewModel { MessageImagePreviewViewModel(get(named("goBackInChatModule")), get(), get()) }
}