package com.gis.featureuserprofile.di

import android.net.Uri
import androidx.fragment.app.FragmentManager
import com.gis.featureuserprofile.navigation.UserProfileCoordinator
import com.gis.featureuserprofile.navigation.UserProfileNavigator
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserbirthdate.ChangeUserBirthdateViewModel
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo.ChangeUserInfoViewModel
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserpassword.ChangeUserPasswordViewModel
import com.gis.featureuserprofile.presentation.ui.userprofile.UserProfileViewModel
import com.gis.featureuserprofile.presentation.ui.extrapermissionsdialog.ExtraPermissionsViewModel
import com.gis.featureuserprofile.presentation.ui.userimagepreview.UserImagePreviewViewModel
import com.gis.featureuserprofile.presentation.ui.imagesourcedialog.ImageSourceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userProfileModule = module {

  single { UserProfileNavigator() }
  single { UserProfileCoordinator(get()) }

  factory(named("showUserProfileScreen")) {
    { fm: FragmentManager, container: Int -> get<UserProfileCoordinator>().start(fm, container) }
  }

  factory(named("updateUserProfileScreen")) {
    { fm: FragmentManager -> get<UserProfileCoordinator>().update(fm) }
  }

  factory(named("showChangeUsernameDialog")) {
    { get<UserProfileCoordinator>().showChangeUsernameDialog() }
  }

  factory(named("showChangeEmailDialog")) {
    { get<UserProfileCoordinator>().showChangeEmailDialog() }
  }

  factory(named("showChangePhoneDialog")) {
    { get<UserProfileCoordinator>().showChangePhoneDialog() }
  }

  factory(named("showChangeBirthdateDialog")) {
    { year: Int, month: Int, day: Int -> get<UserProfileCoordinator>().showChangeBirthdateDialog(year, month, day) }
  }

  factory(named("showChangePasswordDialog")) {
    { get<UserProfileCoordinator>().showChangePasswordDialog() }
  }

  factory(named("showExtraPermissionsDialog")) {
    { get<UserProfileCoordinator>().showExtraPermissionsDialog() }
  }

  factory(named("showImageSourceBottomDialog")) {
    { get<UserProfileCoordinator>().showImageSourceBottomDialog() }
  }

  factory(named("openUserImagePreviewScreen")) {
    { imageUri: Uri -> get<UserProfileCoordinator>().openImagePreviewScreen(imageUri) }
  }

  factory(named("goBack")) {
    { get<UserProfileCoordinator>().goBack() }
  }

  viewModel {
    UserProfileViewModel(
      get(),
      get(),
      get(),
      get(named("showChangeUsernameDialog")),
      get(named("showChangeEmailDialog")),
      get(named("showChangePhoneDialog")),
      get(named("showChangeBirthdateDialog")),
      get(named("showChangePasswordDialog")),
      get(named("showExtraPermissionsDialog")),
      get(named("showImageSourceBottomDialog"))
    )
  }
  viewModel { ChangeUserInfoViewModel(get(), get(), get(), get(), get()) }
  viewModel { ChangeUserBirthdateViewModel(get(), get()) }
  viewModel { ChangeUserPasswordViewModel(get(), get(), get()) }
  viewModel { ExtraPermissionsViewModel() }
  viewModel {
    ImageSourceViewModel(
      get(named("createTempFileForPhotoAndGetUri")),
      get(named("showExtraPermissionsDialog")),
      get(named("openUserImagePreviewScreen"))
    )
  }
  viewModel { UserImagePreviewViewModel(get(), get(), get(named("goBack"))) }
}