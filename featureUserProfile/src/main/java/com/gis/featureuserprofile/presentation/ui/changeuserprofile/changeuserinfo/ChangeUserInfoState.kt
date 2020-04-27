package com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo

import com.gis.repository.domain.entity.User
import com.gis.repository.domain.entity.ValidationResult
import com.gis.repository.domain.entity.ValidationResult.Valid

data class ChangeUserInfoState(
  val loading: Boolean = false,
  val closeDialog: Boolean = false,
  val user: User = User.EMPTY,
  val userInfoValidationError: ValidationResult = Valid,
  val error: Throwable? = null
)


sealed class ChangeUserInfoIntent {
  class ChangeUsername(val username: String, val user: User) : ChangeUserInfoIntent()
  class ChangeEmail(val email: String, val user: User) : ChangeUserInfoIntent()
  class ChangePhone(val phone: String, val user: User) : ChangeUserInfoIntent()
  object Cancel : ChangeUserInfoIntent()
}


sealed class ChangeUserInfoStateChange {
  object StartLoading : ChangeUserInfoStateChange()
  class UserReceived(val user: User) : ChangeUserInfoStateChange()
  object CloseDialog : ChangeUserInfoStateChange()
  class UserInfoValidationError(val validationResult: ValidationResult) : ChangeUserInfoStateChange()
  class Error(val error: Throwable) : ChangeUserInfoStateChange()
  object HideError : ChangeUserInfoStateChange()
}