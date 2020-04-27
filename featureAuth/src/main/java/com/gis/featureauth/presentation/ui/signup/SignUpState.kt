package com.gis.featureauth.presentation.ui.signup

import com.gis.repository.domain.entity.SignUpCredsValidationResult
import com.gis.repository.domain.entity.ValidationResult.*
import com.gis.repository.domain.entity.ValidationResult

data class SignUpState(
  val loading: Boolean = false,
  val showSmsCodeDialog: Boolean = false,
  val signUpCredsValidationResult: SignUpCredsValidationResult = SignUpCredsValidationResult(Valid, Valid, Valid),
  val phoneValidationResult: ValidationResult = ValidationResult.Valid,
  val error: Throwable? = null
)


sealed class SignUpIntent {
  class SignUpWithCreds(val email: String, val username: String, val password: String) : SignUpIntent()
  class SignUpWithPhone(val phone: String) : SignUpIntent()
  object GoToSignInScreen : SignUpIntent()
  object SignUpWithFacebook : SignUpIntent()
  object SignUpWithTwitter : SignUpIntent()
  object SignUpWithInstagram : SignUpIntent()
  object SignUpWithGoogle : SignUpIntent()
}


sealed class SignUpStateChange {
  object StartLoading : SignUpStateChange()
  object FinishLoading : SignUpStateChange()
  object ShowSmsCodeDialog : SignUpStateChange()
  object DontShowSmsCodeDialog : SignUpStateChange()
  class CredsValidationError(val validationResultSignUp: SignUpCredsValidationResult) : SignUpStateChange()
  class PhoneValidationError(val validationResult: ValidationResult) : SignUpStateChange()
  class Error(val error: Throwable) : SignUpStateChange()
  object HideError : SignUpStateChange()
}