package com.gis.featureauthwithphone.presentation.ui.enterphone

import com.gis.repository.domain.entity.Country
import com.gis.repository.domain.entity.ValidationResult

data class EnterPhoneState(
  val loading: Boolean = false,
  val chosenCountry: Country = Country.DEFAULT,
  val formattedPhone: String = "",
  val phoneValidationResult: ValidationResult = ValidationResult.Empty,
  val error: Throwable? = null
)


sealed class EnterPhoneIntent {
  object ObserveCurrentCountry : EnterPhoneIntent()
  class PhoneInputChanged(val phone: String) : EnterPhoneIntent()
  class SendPhone(val phone: String) : EnterPhoneIntent()
  object ChooseCountry : EnterPhoneIntent()
}


sealed class EnterPhoneStateChange {
  class CountryReceived(val country: Country) : EnterPhoneStateChange()
  class PhoneValidated(
    val validationResult: ValidationResult,
    val phoneFormatted: String
  ) : EnterPhoneStateChange()

  class Error(val error: Throwable) : EnterPhoneStateChange()
  object HideError : EnterPhoneStateChange()
}