package com.gis.featurechart.di

import androidx.fragment.app.FragmentManager
import com.gis.featurechart.linechart.LineChartViewModel
import com.gis.featurechart.navigation.LineChartCoordinator
import com.gis.featurechart.navigation.LineChartNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val chartModule = module {

  single { LineChartNavigator() }

  single { LineChartCoordinator(get()) }

  factory(named("showLineChartScreen")) {
    { fm: FragmentManager, container: Int -> get<LineChartCoordinator>().start(fm, container) }
  }

  factory(named("updateLineChartScreen")) {
    { fm: FragmentManager -> get<LineChartCoordinator>().update(fm) }
  }

  viewModel { LineChartViewModel(get(), get()) }
}