package com.gis.featureonboarding.di

import androidx.fragment.app.FragmentManager
import com.gis.featureonboarding.navigation.OnboardingCoordinator
import com.gis.featureonboarding.navigation.OnboardingNavigator
import com.gis.featureonboarding.presentation.onboardingroot.OnboardingRootViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val onboardingModule = module {
  single { OnboardingNavigator() }
  single { OnboardingCoordinator(get()) }

  factory(named("setOnboardingFragmentManager")) {
    { fm: FragmentManager -> get<OnboardingNavigator>().setFragmentManager(fm) }
  }

  factory(named("setOnboardingChildFragmentManager")) {
    { cfm: FragmentManager -> get<OnboardingNavigator>().setChildFragmentManager(cfm) }
  }

  factory(named("setOnboardingFragmentContainer")) {
    { container: Int -> get<OnboardingNavigator>().setFragmentContainer(container) }
  }

  factory(named("startOnboarding")) {
    { showGotItButton: Boolean -> get<OnboardingCoordinator>().start(showGotItButton) }
  }
  factory(named("getFirstOnboardingPage")) { get<OnboardingCoordinator>().getFirstPage() }
  factory(named("getSecondOnboardingPage")) { get<OnboardingCoordinator>().getSecondPage() }
  factory(named("getThirdOnboardingPage")) {
    { showGotItButton: Boolean -> get<OnboardingCoordinator>().getThirdPage(showGotItButton) }
  }

  viewModel { OnboardingRootViewModel() }
}