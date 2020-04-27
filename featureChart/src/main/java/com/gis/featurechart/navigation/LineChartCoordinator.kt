package com.gis.featurechart.navigation

import androidx.fragment.app.FragmentManager

class LineChartCoordinator(private val navigator: LineChartNavigator) {

  fun start(fragmentManager: FragmentManager, fragmentContainer: Int) {
    navigator.setFragmentManager(fragmentManager)
    navigator.setFragmentContainer(fragmentContainer)
    navigator.openLineChartScreen()
  }

  fun update(fragmentManager: FragmentManager) {
    navigator.setFragmentManager(fragmentManager)
  }
}