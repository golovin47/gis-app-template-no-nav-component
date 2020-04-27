package com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserbirthdate

import com.gis.repository.domain.entity.User


data class ChangeUserBirthdateState(
  val loading: Boolean = false,
  val closeDialog: Boolean = false,
  val user: User = User.EMPTY,
  val error: Throwable? = null
)


sealed class ChangeUserBirthdateIntent {
  class ChangeBirthdate(val user: User) : ChangeUserBirthdateIntent()
  object Cancel : ChangeUserBirthdateIntent()
}


sealed class ChangeUserBirthdateStateChange {
  object StartLoading : ChangeUserBirthdateStateChange()
  class UserReceived(val user: User) : ChangeUserBirthdateStateChange()
  object CloseDialog : ChangeUserBirthdateStateChange()
  class Error(val error: Throwable) : ChangeUserBirthdateStateChange()
  object HideError : ChangeUserBirthdateStateChange()
}