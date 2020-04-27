package com.gis.featureauth.navigation

import androidx.fragment.app.FragmentManager
import com.gis.featureauth.R
import com.gis.featureauth.presentation.ui.chooseuser.ChooseUserDialogFragment
import com.gis.featureauth.presentation.ui.signin.SignInFragment
import com.gis.featureauth.presentation.ui.signup.SignUpFragment

class AuthNavigator {

  private var fm: FragmentManager? = null
  private var fragmentContainer: Int = -1

  fun setFragmentManager(fm: FragmentManager?) {
    this.fm = fm
  }

  fun setFragmentContainer(resId: Int) {
    fragmentContainer = resId
  }

  fun openSignUpScreen() {
    removePreviousScreens(fm)
    fm?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, SignUpFragment())
      ?.commit()
  }

  fun openSignInScreen() {
    fm?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, SignInFragment())
      ?.addToBackStack("SignInFragment")
      ?.commit()
  }

  fun showChooseUserDialog() {
    ChooseUserDialogFragment().show(fm!!, "ChooseUserDialogFragment")
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