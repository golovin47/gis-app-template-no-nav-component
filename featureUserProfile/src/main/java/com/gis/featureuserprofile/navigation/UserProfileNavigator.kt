package com.gis.featureuserprofile.navigation

import android.net.Uri
import androidx.fragment.app.FragmentManager
import com.gis.featureuserprofile.R
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserbirthdate.ChangeUserBirthDateDialogFragment
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo.ChangeUserInfoDialogFragment
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo.TYPE_EMAIL
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo.TYPE_PHONE
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo.TYPE_USERNAME
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserpassword.ChangeUserPasswordDialogFragment
import com.gis.featureuserprofile.presentation.ui.extrapermissionsdialog.ExtraPermissionsDialogFragment
import com.gis.featureuserprofile.presentation.ui.userimagepreview.UserImagePreviewFragment
import com.gis.featureuserprofile.presentation.ui.imagesourcedialog.ImageSourceBSDF
import com.gis.featureuserprofile.presentation.ui.userprofile.UserProfileFragment

class UserProfileNavigator {

  private var fm: FragmentManager? = null
  private var fragmentContainer: Int = -1

  fun setFragmentManager(fm: FragmentManager?) {
    this.fm = fm
  }

  fun setFragmentContainer(resId: Int) {
    fragmentContainer = resId
  }

  fun openUserProfileScreen() {
    fm?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, UserProfileFragment())
      ?.addToBackStack("UserProfileFragment")
      ?.commit()
  }

  fun showChangeUsernameDialog() {
    ChangeUserInfoDialogFragment.getInstance(TYPE_USERNAME).show(fm!!, "ChangeUserInfoDialogFragment")
  }

  fun showChangeEmailDialog() {
    ChangeUserInfoDialogFragment.getInstance(TYPE_EMAIL).show(fm!!, "ChangeUserInfoDialogFragment")
  }

  fun showChangePhoneDialog() {
    ChangeUserInfoDialogFragment.getInstance(TYPE_PHONE).show(fm!!, "ChangeUserInfoDialogFragment")
  }

  fun showChangeBirthdateDialog(year: Int, month: Int, day: Int) {
    ChangeUserBirthDateDialogFragment.getInstance(year, month, day).show(fm!!, "ChangeUserBirthDateDialogFragment")
  }

  fun showChangePasswordDialog() {
    ChangeUserPasswordDialogFragment().show(fm!!, "ChangeUserPasswordDialogFragment")
  }

  fun showExtraPermissionsDialog() {
    ExtraPermissionsDialogFragment().show(fm!!, "ExtraPermissionsDialogFragment")
  }

  fun showImageSourceBottomDialog() {
    ImageSourceBSDF().show(fm!!, "ImageSourceBSDF")
  }

  fun openUserImagePreviewScreen(uri: Uri) {
    fm?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, UserImagePreviewFragment.getInstance(uri))
      ?.addToBackStack("UserImagePreviewFragment")
      ?.commit()
  }

  fun goBack() {
    fm?.popBackStack()
  }
}