package com.gis.repository.di

import android.content.Context.MODE_PRIVATE
import com.gis.repository.data.local.datasource.LocalChartDataDataSource
import com.gis.repository.data.local.datasource.LocalChatRoomsDataSource
import com.gis.repository.data.local.datasource.LocalCountriesDataSource
import com.gis.repository.data.local.datasource.LocalUserDataSource
import com.gis.repository.data.remote.api.AppTemplateApiProvider
import com.gis.repository.data.remote.datasource.RemoteChatRoomsDataSource
import com.gis.repository.data.remote.datasource.RemoteConversationDataSource
import com.gis.repository.data.remote.datasource.RemoteGifDataSource
import com.gis.repository.data.repository.*
import com.gis.repository.data.service.AuthServiceImpl
import com.gis.repository.data.service.ChangePasswordServiceImpl
import com.gis.repository.data.service.CountdownServiceImpl
import com.gis.repository.domain.datasource.*
import com.gis.repository.domain.interactors.*
import com.gis.repository.domain.repository.*
import com.gis.repository.domain.service.AuthService
import com.gis.repository.domain.service.ChangePasswordService
import com.gis.repository.domain.service.CountdownService
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repoModule = module {

  //  single { AppTemplateDbProvider.createDb(androidContext()) }

  single { AppTemplateApiProvider.createApi() }

  single { androidContext().getSharedPreferences("sharedPrefs", MODE_PRIVATE) }

  single<UserDataSource>(named("localUserDataSource")) { LocalUserDataSource(get()) }

  single<UserRepository> { UserRepositoryImpl(get(named("localUserDataSource"))) }

  single<AuthService> { AuthServiceImpl(get()) }

  single<ChangePasswordService> { ChangePasswordServiceImpl() }

  single<CountdownService> { CountdownServiceImpl() }

  single<CountriesDataSource>(named("localCountriesDataSource")) {
    LocalCountriesDataSource(
      androidContext().assets,
      androidContext().resources
    )
  }

  single<CountriesRepository> { CountriesRepositoryImpl(get(named("localCountriesDataSource"))) }

  single<ChatRoomsRepository> { ChatRoomsRepositoryImpl(get(named("localChatRoomsDataSource")), get(named("remoteChatRoomsDataSource"))) }

  single<ChatRoomsDataSource>(named("localChatRoomsDataSource")) { LocalChatRoomsDataSource() }

  single<ChatRoomsDataSource>(named("remoteChatRoomsDataSource")) { RemoteChatRoomsDataSource(get()) }

  single<ConversationRepository> { ConversationRepositoryImpl(get(named("remoteConversationDataSource"))) }

  single<ConversationDataSource>(named("remoteConversationDataSource")) { RemoteConversationDataSource(get()) }

  single<GifDataSource>(named("remoteGifDataSource")) { RemoteGifDataSource() }

  single<GifRepository> { GifRepositoryImpl(get(named("remoteGifDataSource"))) }

  single<ChartDataDataSource>(named("localChartDataDataSource")) { LocalChartDataDataSource() }

  single<ChartDataRepository> { ChartDataRepositoryImpl(get(named("localChartDataDataSource"))) }

  /** Interactors */

  //Auth

  factory { SignUpCredsValidationUseCase(get(), get(), get()) }

  factory { SignInCredsValidationUseCase(get(), get()) }

  factory { ChangePasswordValidationUseCase(get()) }

  factory { UsernameValidationUseCase() }

  factory { EmailValidationUseCase() }

  factory { PhoneValidationUseCase(get()) }

  factory { PhoneFormatUseCase(get()) }

  factory { PasswordValidationUseCase() }

  factory { SignUpWithCredsUseCase(get()) }

  factory { SignInWithCredsUseCase(get()) }

  factory { AuthWithPhoneUseCase(get()) }

  factory { AuthWithFingerPrintUseCase(get()) }

  factory { AuthWithFacebookUseCase(get()) }

  factory { AuthWithTwitterUseCase(get()) }

  factory { AuthWithInstagramUseCase(get()) }

  factory { AuthWithGoogleUseCase(get()) }

  factory { SignOutUseCase(get()) }

  //PhoneAuth

  factory { GetCountriesListUseCase(get()) }

  factory { SearchCountriesUseCase(get()) }

  factory { GetChosenCountryUseCase(get()) }

  factory { ChooseCountryUseCase(get()) }

  factory { ObserveCountdownUseCase(get()) }

  factory { StartCountdownUseCase(get()) }

  factory { FinishCountdownUseCase(get()) }

  //UserProfile

  factory { ObserveUserUseCase(get()) }

  factory { UpdateUserUseCase(get()) }

  factory { PutUserUseCase(get()) }

  factory { ChangePasswordUseCase(get()) }

  factory { EnableFingerprintAuthUseCase(get()) }

  factory { ShowFingerprintDialogAndCheckUsers(get()) }

  factory { GetFingerprintUsers(get()) }

  //Chat

  factory { ObserveChatRoomsUseCase(get()) }

  factory { SearchChatRoomsUseCase(get()) }

  factory { RefreshChatRoomsUseCase(get()) }

  factory { LoadNextChatRoomsPageUseCase(get()) }

  factory { AddChatRoomUseCase(get()) }

  factory { UpdateChatRoomUseCase(get()) }

  factory { DeleteChatRoomUseCase(get()) }

  factory { ObserveConversationUseCase(get()) }

  factory { SendChatTextMessageUseCase(get()) }

  factory { SendChatPhotoMessageUseCase(get()) }

  factory { SendChatFileMessageUseCase(get()) }

  factory { DeleteChatMessageUseCase(get()) }

  factory { GetAllGalleryImagesUseCase(get(named("getAllGalleryImages"))) }

  factory { GetGifCategoriesUseCase(get()) }

  factory { GetGifSubCategoriesUseCase(get()) }

  factory { GetCategoryGifsUseCase(get()) }

  factory { GetPopularGifsUseCase(get()) }

  factory { SearchGifsUseCase(get()) }

  //Chart

  factory { ObserveChartDataUseCase(get()) }

  factory { LoadOtherChartDataUseCase(get()) }
}