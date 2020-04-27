package com.gis.featureauthwithphone.di

import com.gis.featureauthwithphone.navigation.AuthWithPhoneCoordinator
import com.gis.featureauthwithphone.navigation.AuthWithPhoneNavigator
import com.gis.featureauthwithphone.presentation.ui.enterphone.EnterPhoneViewModel
import com.gis.featureauthwithphone.presentation.ui.enterphonewithonboarding.EnterPhoneWithOnboardingViewModel
import com.gis.featureauthwithphone.presentation.ui.smscode.SmsCodeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authWithPhoneModule = module {

  single { AuthWithPhoneNavigator() }

  single { AuthWithPhoneCoordinator(get()) }

  factory(named("startAuthWithPhone")) { { get<AuthWithPhoneCoordinator>().start() } }
  factory(named("openSmsCodeScreen")) { { phone: String -> get<AuthWithPhoneCoordinator>().openSmsCodeScreen(phone) } }
  factory(named("closeSmsCode")) { { get<AuthWithPhoneCoordinator>().goBack() } }

  viewModel { EnterPhoneViewModel(get(named("openChooseCountry")), get(named("openSmsCodeScreen")), get(), get(), get()) }

  viewModel {
    EnterPhoneWithOnboardingViewModel(
      get(named("openChooseCountry")),
      get(named("openSmsCodeScreen")),
      get(),
      get(),
      get()
    )
  }

  viewModel { SmsCodeViewModel(get(named("closeSmsCode")), get(), get(), get()) }
}