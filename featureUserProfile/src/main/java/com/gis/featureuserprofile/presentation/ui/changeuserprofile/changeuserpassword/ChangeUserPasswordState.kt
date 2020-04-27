package com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserpassword

import com.gis.repository.domain.entity.ChangePasswordValidationResult
import com.gis.repository.domain.entity.User
import com.gis.repository.domain.entity.ValidationResult.Valid

data class ChangeUserPasswordState(
  val loading: Boolean = false,
  val closeDialog: Boolean = false,
  val user: User = User.EMPTY,
  val passwordValidationResult: ChangePasswordValidationResult = ChangePasswordValidationResult(Valid, Valid),
  val error: Throwable? = null
)


sealed class ChangeUserPasswordIntent {
  class ChangePassword(val oldPassword: String, val newPassword: String) : ChangeUserPasswordIntent()
  object Cancel : ChangeUserPasswordIntent()
}


sealed class ChangeUserPasswordStateChange {
  object StartLoading : ChangeUserPasswordStateChange()
  class UserReceived(val user: User) : ChangeUserPasswordStateChange()
  object CloseDialog : ChangeUserPasswordStateChange()
  class PasswordValidationError(val changePasswordValidationResult: ChangePasswordValidationResult) :
    ChangeUserPasswordStateChange()

  class Error(val error: Throwable) : ChangeUserPasswordStateChange()
  object HideError : ChangeUserPasswordStateChange()
}