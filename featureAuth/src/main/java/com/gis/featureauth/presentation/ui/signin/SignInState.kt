package com.gis.featureauth.presentation.ui.signin

import androidx.fragment.app.FragmentActivity
import com.gis.repository.domain.entity.SignInCredsValidationResult
import com.gis.repository.domain.entity.User
import com.gis.repository.domain.entity.ValidationResult.*
import com.gis.repository.domain.entity.ValidationResult

data class SignInState(
  val loading: Boolean = false,
  val showSmsCodeDialog: Boolean = false,
  val showChooseUserDialog: Boolean = false,
  val authWithUser: Boolean = false,
  val fingerprintUser: User = User.EMPTY,
  val credsValidationResult: SignInCredsValidationResult = SignInCredsValidationResult(Valid, Valid),
  val phoneValidationResult: ValidationResult = Valid,
  val error: Throwable? = null
)


sealed class SignInIntent {
  class SignInWithCreds(val email: String, val password: String) : SignInIntent()
  class SignInWithPhone(val phone: String) : SignInIntent()
  class GetUsersAvailableForFingerprintAuth(val activity: FragmentActivity) : SignInIntent()
  class SignInWithFingerprint(val user: User) : SignInIntent()
  object ShowChooseUserDialog : SignInIntent()
  object SignInWithFacebook : SignInIntent()
  object SignInWithTwitter : SignInIntent()
  object SignInWithInstagram : SignInIntent()
  object SignInWithGoogle : SignInIntent()
  object GoToSignUpScreen : SignInIntent()
}


sealed class SignInStateChange {
  object Idle : SignInStateChange()
  object StartLoading : SignInStateChange()
  object FinishLoading : SignInStateChange()
  object ShowSmsCodeDialog : SignInStateChange()
  object DontShowSmsCodeDialog : SignInStateChange()
  object ShowChooseUserDialog : SignInStateChange()
  object DontShowChooseUserDialog : SignInStateChange()
  class AuthWithUser(val user: User) : SignInStateChange()
  object DontAuthWithUser : SignInStateChange()
  class CredsValidationError(val validationResult: SignInCredsValidationResult) : SignInStateChange()
  class PhoneValidationError(val validationResult: ValidationResult) : SignInStateChange()
  class Error(val error: Throwable) : SignInStateChange()
  object HideError : SignInStateChange()
}