package com.gis.featuremainscreen.di

import com.gis.featuremainscreen.navigation.MainScreenCoordinator
import com.gis.featuremainscreen.navigation.MainScreenNavigator
import com.gis.featuremainscreen.presentation.ui.mainscreen.MainScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainScreenModule = module {

  single {
    MainScreenNavigator(
      get(named("showUserProfileScreen")),
      get(named("showLineChartScreen")),
      get(named("updateLineChartScreen")),
      get(named("showChatRoomsScreen")),
      get(named("updateChatRoomsScreen")),
      get(named("showUserProfileScreen")),
      get(named("updateUserProfileScreen"))
    )
  }

  single { MainScreenCoordinator(get()) }

  factory(named("showFeed")) {
    { get<MainScreenCoordinator>().showFeed() }
  }

  factory(named("showChart")) {
    { get<MainScreenCoordinator>().showChart() }
  }

  factory(named("showChat")) {
    { get<MainScreenCoordinator>().showChat() }
  }

  factory(named("showProfile")) {
    { get<MainScreenCoordinator>().showProfile() }
  }

  viewModel {
    MainScreenViewModel(
      get(named("showFeed")),
      get(named("showChart")),
      get(named("showChat")),
      get(named("showProfile"))
    )
  }
}