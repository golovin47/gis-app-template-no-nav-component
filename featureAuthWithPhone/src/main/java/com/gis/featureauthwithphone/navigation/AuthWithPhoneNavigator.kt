package com.gis.featureauthwithphone.navigation

import androidx.fragment.app.FragmentManager
import com.gis.featureauthwithphone.R
import com.gis.featureauthwithphone.presentation.ui.enterphone.EnterPhoneFragment
import com.gis.featureauthwithphone.presentation.ui.enterphonewithonboarding.EnterPhoneWithOnboardingFragment
import com.gis.featureauthwithphone.presentation.ui.smscode.SmsCodeFragment

class AuthWithPhoneNavigator {

  private var fm: FragmentManager? = null
  private var fragmentContainer: Int = -1

  fun setFragmentManager(fm: FragmentManager?) {
    this.fm = fm
  }

  fun setFragmentContainer(resId: Int) {
    fragmentContainer = resId
  }

  fun openEnterPhoneScreen() {
    removePreviousScreens(fm)
    fm?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, EnterPhoneFragment())
      ?.commit()
  }

  fun openEnterPhoneWithOnboardingScreen() {
    removePreviousScreens(fm)
    fm?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, EnterPhoneWithOnboardingFragment())
      ?.commit()
  }

  fun openSmsCodeScreen(phone: String) {
    fm?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, SmsCodeFragment.getInstance(phone))
      ?.addToBackStack("SmsCodeFragment")
      ?.commit()
  }

  fun goBack() {
    fm?.popBackStack()
  }

  private fun removePreviousScreens(fragmentManager: FragmentManager?) {
    if (!fragmentManager?.isStateSaved!!) {
      fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
      if (fragmentManager.fragments.size != 0) {
        for (f in fragmentManager.fragments) {
          fragmentManager.beginTransaction().remove(f).commit()
        }
      }
    }
  }
}