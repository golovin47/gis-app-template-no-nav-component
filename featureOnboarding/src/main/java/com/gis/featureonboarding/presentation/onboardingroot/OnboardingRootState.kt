package com.gis.featureonboarding.presentation.onboardingroot

const val FIRST_ONBOARDING_PAGE = 0
const val SECOND_ONBOARDING_PAGE = 1
const val THIRD_ONBOARDING_PAGE = 2

data class OnboardingRootState(
  val curPage: Int = FIRST_ONBOARDING_PAGE,
  val showInitAnimation: Boolean = false
)


sealed class OnboardingRootIntent {
  class ChangeCurPage(val page: Int) : OnboardingRootIntent()
  object DontShowInitAnimation : OnboardingRootIntent()
}


sealed class OnboardingRootStateChange {
  class CurPageChanged(val page: Int) : OnboardingRootStateChange()
  object ShowInitAnimation : OnboardingRootStateChange()
  object InitAnimationShown : OnboardingRootStateChange()
}