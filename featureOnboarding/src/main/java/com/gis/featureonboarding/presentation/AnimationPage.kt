package com.gis.featureonboarding.presentation

interface AnimationPage {

  fun playAnimation()

  fun pauseAnimation()

  fun animateTo(to: Float)
}