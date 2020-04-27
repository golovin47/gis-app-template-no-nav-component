package com.gis.featureonboarding.navigation

import com.gis.featureonboarding.presentation.AnimationPage

class OnboardingCoordinator(private val onboardingNavigator: OnboardingNavigator) {

  fun start(showGotItButton: Boolean) {
    onboardingNavigator.start(showGotItButton)
  }

  fun getFirstPage(): AnimationPage = onboardingNavigator.getFirstPage()

  fun getSecondPage(): AnimationPage = onboardingNavigator.getSecondPage()

  fun getThirdPage(showGotItButton:Boolean): AnimationPage = onboardingNavigator.getThirdPage(showGotItButton)
}