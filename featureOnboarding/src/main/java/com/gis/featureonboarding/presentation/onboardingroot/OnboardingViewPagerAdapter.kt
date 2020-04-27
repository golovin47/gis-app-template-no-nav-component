package com.gis.featureonboarding.presentation.onboardingroot

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.gis.featureonboarding.presentation.AnimationPage

class OnboardingViewPagerAdapter(fm: FragmentManager, val pages: List<AnimationPage>) : FragmentPagerAdapter(fm) {

  override fun getCount(): Int = pages.size

  override fun getItem(position: Int): Fragment = pages[position] as Fragment
}