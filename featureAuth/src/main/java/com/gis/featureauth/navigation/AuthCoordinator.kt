package com.gis.featureauth.navigation

class AuthCoordinator(private val navigator: AuthNavigator) {

  fun start() {
    navigator.openSignUpScreen()
  }

  fun fromSignUpToSignIn() {
    navigator.openSignInScreen()
  }

  fun fromSignInToSignUp() {
    navigator.goBack()
  }

  fun showChooseUserDialogFragment() {
    navigator.showChooseUserDialog()
  }
}