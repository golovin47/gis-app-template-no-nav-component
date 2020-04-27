package com.gis.repository.domain.entity

class SignUpCredsValidationResult(
  val emailValidation: ValidationResult,
  val usernameValidation: ValidationResult,
  val passwordValidation: ValidationResult
)

class SignInCredsValidationResult(
  val emailValidation: ValidationResult,
  val passwordValidation: ValidationResult
)

class ChangePasswordValidationResult(
  val oldPasswordValidation: ValidationResult,
  val newPasswordValidation: ValidationResult
)

sealed class ValidationResult {
  object Empty : ValidationResult()
  object TooShort : ValidationResult()
  object Valid : ValidationResult()
  object Invalid : ValidationResult()
}