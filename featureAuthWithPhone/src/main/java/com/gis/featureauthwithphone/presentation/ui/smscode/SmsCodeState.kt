package com.gis.featureauthwithphone.presentation.ui.smscode

data class SmsCodeState(
  val loading: Boolean = false,
  val counter: Int = -1,
  val codeError: Boolean = false,
  val error: Throwable? = null
)


sealed class SmsCodeIntent {
  object HideSmsCodeError : SmsCodeIntent()
  class SendCode(val phone: String, val code: String) : SmsCodeIntent()
  object ResendCode : SmsCodeIntent()
  object GoBack : SmsCodeIntent()
}


sealed class SmsCodeStateChange {
  object Idle : SmsCodeStateChange()
  object StartLoading : SmsCodeStateChange()
  class CountdownChanged(val count: Int) : SmsCodeStateChange()
  class Error(val error: Throwable) : SmsCodeStateChange()
  object HideError : SmsCodeStateChange()
}