package com.gis.featurechoosecountry.di

import com.gis.featurechoosecountry.navigation.ChooseCountryCoordinator
import com.gis.featurechoosecountry.navigation.ChooseCountryNavigator
import com.gis.featurechoosecountry.presentation.ui.choosecountry.ChooseCountryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val chooseCountryModule = module {

  single { ChooseCountryNavigator() }
  single { ChooseCountryCoordinator(get()) }

  factory((named("openChooseCountry"))) { { get<ChooseCountryCoordinator>().start() } }
  factory((named("closeChooseCountry"))) { { get<ChooseCountryCoordinator>().finish() } }

  viewModel { ChooseCountryViewModel(get(named("closeChooseCountry")), get(), get(), get()) }
}