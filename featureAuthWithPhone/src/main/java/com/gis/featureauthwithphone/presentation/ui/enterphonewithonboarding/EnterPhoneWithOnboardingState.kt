package com.gis.featureauthwithphone.presentation.ui.enterphonewithonboarding

import com.gis.repository.domain.entity.Country
import com.gis.repository.domain.entity.ValidationResult

data class EnterPhoneWithOnboardingState(
  val loading: Boolean = false,
  val chosenCountry: Country = Country.DEFAULT,
  val formattedPhone: String = "",
  val phoneValidationResult: ValidationResult = ValidationResult.Empty,
  val error: Throwable? = null
)


sealed class EnterPhoneWithOnboardingIntent {
  object ObserveCurrentCountry : EnterPhoneWithOnboardingIntent()
  class PhoneInputChanged(val phone: String) : EnterPhoneWithOnboardingIntent()
  class SendPhone(val phone: String) : EnterPhoneWithOnboardingIntent()
  object ChooseCountry : EnterPhoneWithOnboardingIntent()
}


sealed class EnterPhoneWithOnboardingStateChange {
  class CountryReceived(val country: Country) : EnterPhoneWithOnboardingStateChange()
  class PhoneValidated(
    val validationResult: ValidationResult,
    val phoneFormatted: String
  ) : EnterPhoneWithOnboardingStateChange()

  class Error(val error: Throwable) : EnterPhoneWithOnboardingStateChange()
  object HideError : EnterPhoneWithOnboardingStateChange()
}