package com.gis.repository.domain.interactors

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.gis.repository.domain.entity.ChatMessage.*
import com.gis.repository.domain.entity.ChatRoom
import com.gis.repository.domain.entity.FileMeta
import com.gis.repository.domain.repository.ChatRoomsRepository
import com.gis.repository.domain.repository.ConversationRepository
import com.gis.repository.domain.repository.GifRepository
import io.reactivex.Observable

class ObserveChatRoomsUseCase(private val repository: ChatRoomsRepository) {
  fun execute() = repository.observeChatRooms()
}


class SearchChatRoomsUseCase(private val repository: ChatRoomsRepository) {
  fun execute(search: String) = repository.searchChatRooms(search)
}


class RefreshChatRoomsUseCase(private val repository: ChatRoomsRepository) {
  fun execute() = repository.refreshChatRooms()
}


class LoadNextChatRoomsPageUseCase(private val repository: ChatRoomsRepository) {
  fun execute(pageNum: Int, perPage: Int = 20) = repository.loadNextPage(pageNum, perPage)
}


class AddChatRoomUseCase(private val repository: ChatRoomsRepository) {
  fun execute(chatRoom: ChatRoom) = repository.addChatRoom(chatRoom)
}


class UpdateChatRoomUseCase(private val repository: ChatRoomsRepository) {
  fun execute(chatRoom: ChatRoom) = repository.updateChatRoom(chatRoom)
}


class DeleteChatRoomUseCase(private val repository: ChatRoomsRepository) {
  fun execute(chatId: Int) = repository.deleteChatRoom(chatId)
}

class ObserveConversationUseCase(private val repository: ConversationRepository) {
  fun execute(id: Int) = repository.observeConversation(id)
}

class SendChatTextMessageUseCase(private val repository: ConversationRepository) {
  fun execute(chatTextMessage: ChatTextMessage) = repository.sendTextMessage(chatTextMessage)
}

class SendChatPhotoMessageUseCase(private val repository: ConversationRepository) {
  fun execute(chatPhotoMessage: ChatPhotoMessage) = repository.sendPhotoMessage(chatPhotoMessage)
}

class SendChatFileMessageUseCase(private val repository: ConversationRepository) {
  fun execute(chatFileMessage: ChatFileMessage) = repository.sendFileMessage(chatFileMessage)
}

class DeleteChatMessageUseCase(private val repository: ConversationRepository) {
  fun execute(id: Int) = repository.removeMessage(id)
}

class GetAllGalleryImagesUseCase(private val getAllGalleryImages: () -> Observable<List<String>>) {
  fun execute() = getAllGalleryImages.invoke()
}

class GetGifCategoriesUseCase(private val gifRepository: GifRepository) {
  fun execute(limit: Int? = null, offset: Int? = null, sort: String? = null) =
    gifRepository.getCategories(limit, offset, sort)
}

class GetGifSubCategoriesUseCase(private val gifRepository: GifRepository) {
  fun execute(categoryEncodedName: String, limit: Int? = null, offset: Int? = null, sort: String? = null) =
    gifRepository.getSubCategories(categoryEncodedName, limit, offset, sort)
}

class GetCategoryGifsUseCase(private val gifRepository: GifRepository) {
  fun execute(
    categoryEncodedName: String,
    subCategoryEncodedName: String,
    limit: Int? = null,
    offset: Int? = null,
    lang: String? = null
  ) = gifRepository.getCategoryGifs(categoryEncodedName, subCategoryEncodedName, limit, offset, lang)
}

class GetPopularGifsUseCase(private val gifRepository: GifRepository) {
  fun execute(limit: Int? = null, offset: Int? = null) = gifRepository.getPopularGifs(limit, offset)
}

class SearchGifsUseCase(private val gifRepository: GifRepository) {
  fun execute(query: String, limit: Int? = null, offset: Int? = null, lang: String? = null) =
    gifRepository.search(query, limit, offset, lang)
}