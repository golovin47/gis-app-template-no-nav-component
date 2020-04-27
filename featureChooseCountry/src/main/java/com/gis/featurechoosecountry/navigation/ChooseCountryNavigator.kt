package com.gis.featurechoosecountry.navigation

import androidx.fragment.app.FragmentManager
import com.gis.featurechoosecountry.R
import com.gis.featurechoosecountry.presentation.ui.choosecountry.ChooseCountryFragment

class ChooseCountryNavigator {

  private var fm: FragmentManager? = null
  private var fragmentContainer: Int = -1

  fun setFragmentManager(fm: FragmentManager?) {
    this.fm = fm
  }

  fun setFragmentContainer(resId: Int) {
    fragmentContainer = resId
  }

  fun openChooseCountryScreen() {
    fm?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_slide_up, R.anim.anim_slide_down, R.anim.anim_slide_up, R.anim.anim_slide_down)
      ?.add(fragmentContainer, ChooseCountryFragment())
      ?.addToBackStack("ChooseCountryFragment")
      ?.commit()
  }

  fun goBack() {
    fm?.popBackStack()
  }
}