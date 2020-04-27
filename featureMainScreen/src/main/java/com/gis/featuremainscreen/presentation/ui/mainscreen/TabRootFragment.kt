package com.gis.featuremainscreen.presentation.ui.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gis.utils.presentation.BackPressHandler
import com.gis.featuremainscreen.R

class TabRootFragment : Fragment(), BackPressHandler {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_tab_root, container, false)
  }

  override fun onBackPress(): Boolean {
    val entries = childFragmentManager.backStackEntryCount
    if (entries > 1) {
      childFragmentManager.popBackStackImmediate()
      return true
    }
    return false
  }
}