package com.gis.featuremainscreen.presentation.ui.mainscreen

data class MainScreenState(val curTab:MainTab = MainTab.FEED)


sealed class MainScreenIntent {
  class ChangeTab(val tab: MainTab) : MainScreenIntent()
}


sealed class MainScreenStateChange {
  class TabChanged(val tab: MainTab) : MainScreenStateChange()
}


enum class MainTab {
  FEED, FRIENDS, CHAT, PROFILE
}