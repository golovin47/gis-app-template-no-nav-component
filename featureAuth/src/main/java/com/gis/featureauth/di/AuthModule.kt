package com.gis.featureauth.di

import com.gis.repository.data.session.SessionManagerImpl
import com.gis.repository.domain.session.SessionManager
import com.gis.featureauth.navigation.AuthCoordinator
import com.gis.featureauth.navigation.AuthNavigator
import com.gis.featureauth.presentation.ui.chooseuser.ChooseUserViewModel
import com.gis.featureauth.presentation.ui.signin.SignInViewModel
import com.gis.featureauth.presentation.ui.signup.SignUpViewModel
import com.gis.featureauth.presentation.ui.smscode.SmsCodeDialogViewModel
import io.reactivex.schedulers.Schedulers.single
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {

  single { AuthNavigator() }
  single { AuthCoordinator(get()) }
  single<SessionManager> { SessionManagerImpl(get()) }

  factory(named("fromSignUpToSignIn")) { { get<AuthCoordinator>().fromSignUpToSignIn() } }

  factory(named("fromSignInToSignUp")) { { get<AuthCoordinator>().fromSignInToSignUp() } }

  factory(named("showChooseUserDialog")) { { get<AuthCoordinator>().showChooseUserDialogFragment() } }

  viewModel { SignUpViewModel(get(named("fromSignUpToSignIn")), get(), get(), get(), get(), get(), get(), get()) }
  viewModel {
    SignInViewModel(
      get(named("fromSignInToSignUp")),
      get(named("showChooseUserDialog")),
      get(),
      get(),
      get(),
      get(),
      get(),
      get(),
      get(),
      get(),
      get()
    )
  }
  viewModel { SmsCodeDialogViewModel(get()) }
  viewModel { ChooseUserViewModel(get(), get()) }
}