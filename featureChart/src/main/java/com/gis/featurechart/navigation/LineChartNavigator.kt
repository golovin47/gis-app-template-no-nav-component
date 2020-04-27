package com.gis.featurechart.navigation

import androidx.fragment.app.FragmentManager
import com.gis.featurechart.presentation.ui.linechart.LineChartFragment
import com.gis.featurefriends.R

class LineChartNavigator {

  private var fragmentManager: FragmentManager? = null
  private var fragmentContainer: Int = -1

  fun setFragmentManager(fragmentManager: FragmentManager) {
    this.fragmentManager = fragmentManager
  }

  fun setFragmentContainer(fragmentContainer: Int) {
    this.fragmentContainer = fragmentContainer
  }

  fun openLineChartScreen() {
    fragmentManager?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, LineChartFragment())
      ?.addToBackStack("LineChartFragment")
      ?.commit()
  }
}