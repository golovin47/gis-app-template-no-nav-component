package com.gis.featureonboarding.navigation

import androidx.fragment.app.FragmentManager
import com.gis.featureonboarding.R
import com.gis.featureonboarding.presentation.AnimationPage
import com.gis.featureonboarding.presentation.onboarding.OnboardingFragment
import com.gis.featureonboarding.presentation.onboardingroot.OnboardingRootFragment

class OnboardingNavigator {

  private var fm: FragmentManager? = null
  private var cfm: FragmentManager? = null
  private var fragmentContainer: Int = -1

  fun setFragmentManager(fm: FragmentManager?) {
    this.fm = fm
  }

  fun setChildFragmentManager(cfm: FragmentManager?) {
    this.cfm = cfm
  }

  fun setFragmentContainer(resId: Int) {
    fragmentContainer = resId
  }

  fun start(showGotItButton: Boolean) {
    fm?.beginTransaction()
      ?.replace(fragmentContainer, OnboardingRootFragment.getInstance(showGotItButton))
      ?.commit()
  }

  fun getFirstPage(): AnimationPage {
    val pages = cfm?.fragments!!
    return if (pages.isNotEmpty()) pages[0] as AnimationPage
    else OnboardingFragment.getInstance(
      titleRes = R.string.first_page_title,
      iconRes = R.raw.onboarding_one,
      showGotItButton = false
    )
  }

  fun getSecondPage(): AnimationPage {
    val pages = cfm?.fragments!!
    return if (pages.isNotEmpty()) pages[1] as AnimationPage
    else OnboardingFragment.getInstance(
      titleRes = R.string.second_page_title,
      iconRes = R.raw.onboarding_two,
      showGotItButton = false
    )
  }

  fun getThirdPage(showGotItButton: Boolean): AnimationPage {
    val pages = cfm?.fragments!!
    return if (pages.isNotEmpty()) pages[2] as AnimationPage
    else OnboardingFragment.getInstance(
      titleRes = R.string.third_page_title,
      iconRes = R.raw.onboarding_three,
      showGotItButton = showGotItButton
    )
  }
}