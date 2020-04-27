package com.gis.featureauth.presentation.ui.smscode

data class SmsCodeState(
  val loading: Boolean = false,
  val closeDialog: Boolean = false,
  val error: Throwable? = null
)

sealed class SmsCodeIntent {
  class Send(val code: String, val phone: String) : SmsCodeIntent()
  object Cancel : SmsCodeIntent()
}

sealed class SmsCodeStateChange {
  object StartLoading : SmsCodeStateChange()
  object CloseDialog : SmsCodeStateChange()
  class Error(val error: Throwable) : SmsCodeStateChange()
  object HideError : SmsCodeStateChange()
}