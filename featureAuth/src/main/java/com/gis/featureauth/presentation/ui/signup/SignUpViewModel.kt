package com.gis.featureauth.presentation.ui.signup

import com.gis.repository.domain.entity.SignUpCredsValidationResult
import com.gis.repository.domain.entity.ValidationResult.Valid
import com.gis.repository.domain.interactors.*
import com.gis.utils.presentation.BaseViewModel
import com.gis.featureauth.presentation.ui.signup.SignUpIntent.*
import com.gis.featureauth.presentation.ui.signup.SignUpStateChange.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class SignUpViewModel(
  private var goToSignInScreen: (() -> Unit)?,
  private val signUpCredsValidationUseCase: SignUpCredsValidationUseCase,
  private val phoneValidationUseCase: PhoneValidationUseCase,
  private val signUpWithCreds: SignUpWithCredsUseCase,
  private val authWithFacebookUseCase: AuthWithFacebookUseCase,
  private val authWithTwitterUseCase: AuthWithTwitterUseCase,
  private val authWithInstagramUseCase: AuthWithInstagramUseCase,
  private val authWithGoogleUseCase: AuthWithGoogleUseCase
) : BaseViewModel<SignUpState>() {

  override fun initState(): SignUpState = SignUpState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(listOf(

      intentStream.ofType(SignUpWithCreds::class.java)
        .switchMap { event ->
          signUpCredsValidationUseCase.validate(event.email, event.username, event.password)
            .switchMap { result ->
              if (result.emailValidation == Valid &&
                result.usernameValidation == Valid &&
                result.passwordValidation == Valid
              )
                signUpWithCreds.execute(event.email, event.username, event.password)
                  .startWith(Observable.just(StartLoading))
                  .cast(SignUpStateChange::class.java)
                  .onErrorResumeNext { e: Throwable -> handleError(e) }
                  .subscribeOn(Schedulers.io())
              else
                Observable.just(CredsValidationError(result))
            }
        },

      intentStream.ofType(SignUpWithPhone::class.java)
        .switchMap { event ->
          phoneValidationUseCase.validate(event.phone)
            .switchMap { result ->
              if (result == Valid)
                Observable.just(ShowSmsCodeDialog, DontShowSmsCodeDialog)
              else
                Observable.just(PhoneValidationError(result))
            }
        },

      intentStream.ofType(SignUpWithFacebook::class.java)
        .switchMap {
          authWithFacebookUseCase.execute()
            .toSingleDefault(FinishLoading)
            .toObservable()
            .cast(SignUpStateChange::class.java)
            .startWith(Observable.just(StartLoading))
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
        },

      intentStream.ofType(SignUpWithTwitter::class.java)
        .switchMap {
          authWithTwitterUseCase.execute()
            .toSingleDefault(FinishLoading)
            .toObservable()
            .cast(SignUpStateChange::class.java)
            .startWith(Observable.just(StartLoading))
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
        },

      intentStream.ofType(SignUpWithInstagram::class.java)
        .switchMap {
          authWithInstagramUseCase.execute()
            .toSingleDefault(FinishLoading)
            .toObservable()
            .cast(SignUpStateChange::class.java)
            .startWith(Observable.just(StartLoading))
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
        },

      intentStream.ofType(SignUpWithGoogle::class.java)
        .switchMap {
          authWithGoogleUseCase.execute()
            .toSingleDefault(FinishLoading)
            .toObservable()
            .cast(SignUpStateChange::class.java)
            .startWith(Observable.just(StartLoading))
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
        },

      intentStream.ofType(GoToSignInScreen::class.java)
        .doOnNext { goToSignInScreen?.invoke() }
    ))

  private fun handleError(error: Throwable) = Observable.just(Error(error), HideError)

  override fun reduceState(previousState: SignUpState, stateChange: Any): SignUpState =
    when (stateChange) {
      is StartLoading -> previousState.copy(
        loading = true,
        signUpCredsValidationResult = SignUpCredsValidationResult(Valid, Valid, Valid),
        phoneValidationResult = Valid,
        error = null
      )

      is FinishLoading -> previousState.copy(loading = false)

      is ShowSmsCodeDialog -> previousState.copy(showSmsCodeDialog = true)

      is DontShowSmsCodeDialog -> previousState.copy(showSmsCodeDialog = false)

      is CredsValidationError -> previousState.copy(
        loading = false,
        signUpCredsValidationResult = stateChange.validationResultSignUp,
        phoneValidationResult = Valid
      )

      is PhoneValidationError -> previousState.copy(
        loading = false,
        signUpCredsValidationResult = SignUpCredsValidationResult(Valid, Valid, Valid),
        phoneValidationResult = stateChange.validationResult
      )

      is Error -> previousState.copy(
        loading = false,
        error = stateChange.error
      )

      is HideError -> previousState.copy(error = null)

      else -> previousState
    }

  override fun onCleared() {
    goToSignInScreen = null
    super.onCleared()
  }
}