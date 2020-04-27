package com.gis.featureauthwithphone.navigation

class AuthWithPhoneCoordinator(private val navigator: AuthWithPhoneNavigator) {

  fun start() {
    navigator.openEnterPhoneScreen()
  }

  fun startWithOnboarding() {
    navigator.openEnterPhoneWithOnboardingScreen()
  }

  fun openSmsCodeScreen(phone: String) {
    navigator.openSmsCodeScreen(phone)
  }

  fun goBack() {
    navigator.goBack()
  }
}