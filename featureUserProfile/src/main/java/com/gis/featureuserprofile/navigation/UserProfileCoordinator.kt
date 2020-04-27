package com.gis.featureuserprofile.navigation

import android.net.Uri
import androidx.fragment.app.FragmentManager

class UserProfileCoordinator(private val userProfileNavigator: UserProfileNavigator) {

  fun start(fragmentManager: FragmentManager, fragmentContainer: Int) {
    userProfileNavigator.setFragmentManager(fragmentManager)
    userProfileNavigator.setFragmentContainer(fragmentContainer)
    userProfileNavigator.openUserProfileScreen()
  }

  fun update(fragmentManager: FragmentManager) {
    userProfileNavigator.setFragmentManager(fragmentManager)
  }

  fun showChangeUsernameDialog() {
    userProfileNavigator.showChangeUsernameDialog()
  }

  fun showChangeEmailDialog() {
    userProfileNavigator.showChangeEmailDialog()
  }

  fun showChangePhoneDialog() {
    userProfileNavigator.showChangePhoneDialog()
  }

  fun showChangeBirthdateDialog(year: Int, month: Int, day: Int) {
    userProfileNavigator.showChangeBirthdateDialog(year, month, day)
  }

  fun showChangePasswordDialog() {
    userProfileNavigator.showChangePasswordDialog()
  }

  fun showExtraPermissionsDialog() {
    userProfileNavigator.showExtraPermissionsDialog()
  }

  fun showImageSourceBottomDialog() {
    userProfileNavigator.showImageSourceBottomDialog()
  }

  fun openImagePreviewScreen(uri: Uri) {
    userProfileNavigator.openUserImagePreviewScreen(uri)
  }

  fun goBack() {
    userProfileNavigator.goBack()
  }
}