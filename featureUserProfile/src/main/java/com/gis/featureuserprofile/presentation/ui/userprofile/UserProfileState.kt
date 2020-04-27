package com.gis.featureuserprofile.presentation.ui.userprofile

import com.gis.repository.domain.entity.User

data class UserProfileState(
  val ctblExpanded: Boolean = true,
  val user: User = User.EMPTY,
  val error: Throwable? = null
)


sealed class UserProfileIntent {
  object StartObserveUser : UserProfileIntent()
  object ExpandCtbl : UserProfileIntent()
  object CollapseCtbl : UserProfileIntent()
  object ChangeUsername : UserProfileIntent()
  object ChangeEmail : UserProfileIntent()
  object ChangePhone : UserProfileIntent()
  class ChangeBirthDate(val year: Int, val month: Int, val day: Int) : UserProfileIntent()
  class EnableFingerprint(val enable: Boolean) : UserProfileIntent()
  object ChangePassword : UserProfileIntent()
  object ChangeAvatarUrl : UserProfileIntent()
  object SignOut : UserProfileIntent()
}


sealed class UserProfileStateChange {
  object Idle : UserProfileStateChange()
  object CtblExpanded : UserProfileStateChange()
  object CtblCollapsed : UserProfileStateChange()
  class UserReceived(val user: User) : UserProfileStateChange()
  class Error(val error: Throwable) : UserProfileStateChange()
  object HideError : UserProfileStateChange()
}