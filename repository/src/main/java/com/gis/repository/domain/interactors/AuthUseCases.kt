package com.gis.repository.domain.interactors

import android.util.Patterns
import androidx.fragment.app.FragmentActivity
import com.gis.repository.domain.entity.*
import com.gis.repository.domain.entity.ValidationResult.*
import com.gis.repository.domain.service.AuthService
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.reactivex.Observable

class SignUpCredsValidationUseCase(
  private val usernameValidationUseCase: UsernameValidationUseCase,
  private val emailValidationUseCase: EmailValidationUseCase,
  private val passwordValidationUseCase: PasswordValidationUseCase
) {
  fun validate(email: String, username: String, password: String): Observable<SignUpCredsValidationResult> =
    Observable.just(
      SignUpCredsValidationResult(
        emailValidation = emailValidationUseCase.validate(email).blockingFirst(),
        usernameValidation = usernameValidationUseCase.validate(username).blockingFirst(),
        passwordValidation = passwordValidationUseCase.validate(password).blockingFirst()
      )
    )
}


class SignInCredsValidationUseCase(
  private val emailValidationUseCase: EmailValidationUseCase,
  private val passwordValidationUseCase: PasswordValidationUseCase
) {
  fun validate(email: String, password: String): Observable<SignInCredsValidationResult> =
    Observable.just(
      SignInCredsValidationResult(
        emailValidation = emailValidationUseCase.validate(email).blockingFirst(),
        passwordValidation = passwordValidationUseCase.validate(password).blockingFirst()
      )
    )
}


class ChangePasswordValidationUseCase(private val passwordValidationUseCase: PasswordValidationUseCase) {
  fun validate(oldPassword: String, newPassword: String) =
    Observable.just(
      ChangePasswordValidationResult(
        oldPasswordValidation = passwordValidationUseCase.validate(oldPassword).blockingFirst(),
        newPasswordValidation = passwordValidationUseCase.validate(newPassword).blockingFirst()
      )
    )
}


class UsernameValidationUseCase {
  fun validate(username: String): Observable<ValidationResult> =
    Observable.just(
      when {
        username.isBlank() -> Empty
        username.length < 6 -> TooShort
        else -> Valid
      }
    )
}


class EmailValidationUseCase {
  fun validate(email: String): Observable<ValidationResult> =
    Observable.just(
      when {
        email.isBlank() -> Empty
        Patterns.EMAIL_ADDRESS.matcher(email).matches() -> Valid
        else -> Invalid
      }
    )
}


class PhoneValidationUseCase(private val phoneUtil: PhoneNumberUtil) {
  fun validate(phone: String): Observable<ValidationResult> =
    Observable.just(
      when {
        phone.isBlank() -> Empty
        phone.isNotBlank() -> try {
          val isValid = phoneUtil.isValidNumber(phoneUtil.parse(phone, null))
          if (isValid) Valid else Invalid
        } catch (e: Exception) {
          Invalid
        }
        else -> Invalid
      }
    )
}


class PhoneFormatUseCase(private val phoneUtil: PhoneNumberUtil) {
  fun execute(phone: String): Observable<String> =
    Observable.fromCallable {
      try {
        val phoneNumber = phoneUtil.parse(phone, null)
        return@fromCallable phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
          .replaceBefore("(", "")
      } catch (e: Exception) {
        phone
      }
    }
}


class PasswordValidationUseCase {
  fun validate(password: String): Observable<ValidationResult> =
    Observable.just(
      when {
        password.isBlank() -> Empty
        password.length < 8 -> TooShort
        else -> Valid
      }
    )
}


class SignUpWithCredsUseCase(private val authService: AuthService) {
  fun execute(email: String, username: String, password: String) =
    authService.signUpWithCreds(email, username, password)
}


class SignInWithCredsUseCase(private val authService: AuthService) {
  fun execute(email: String, password: String) =
    authService.signInWithCreds(email, password)
}


class AuthWithPhoneUseCase(private val authService: AuthService) {
  fun execute(phone: String, code: String) = authService.authWithPhone(phone, code)
}


class AuthWithFingerPrintUseCase(private val authService: AuthService) {
  fun execute(user: User) = authService.authWithFingerprint(user)
}


class AuthWithFacebookUseCase(private val authService: AuthService) {
  fun execute() = authService.authWithFacebook()
}


class AuthWithTwitterUseCase(private val authService: AuthService) {
  fun execute() = authService.authWithTwitter()
}


class AuthWithInstagramUseCase(private val authService: AuthService) {
  fun execute() = authService.authWithInstagram()
}


class AuthWithGoogleUseCase(private val authService: AuthService) {
  fun execute() = authService.authWithGoogle()
}


class SignOutUseCase(private val authService: AuthService) {
  fun execute() = authService.signOut()
}