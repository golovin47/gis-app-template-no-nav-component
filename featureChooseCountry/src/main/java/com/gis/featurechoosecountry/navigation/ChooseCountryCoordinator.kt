package com.gis.featurechoosecountry.navigation

class ChooseCountryCoordinator(private val navigator: ChooseCountryNavigator) {

  fun start() {
    navigator.openChooseCountryScreen()
  }

  fun finish() {
    navigator.goBack()
  }
}