package com.gis.featuremainscreen.navigation

class MainScreenCoordinator(private val mainScreenNavigator: MainScreenNavigator) {

  fun start() {
    mainScreenNavigator.showMainScreen()
  }

  fun showFeed() {
    mainScreenNavigator.showFeed()
  }

  fun showChart() {
    mainScreenNavigator.showFriends()
  }

  fun showChat() {
    mainScreenNavigator.showChat()
  }

  fun showProfile() {
    mainScreenNavigator.showProfile()
  }
}