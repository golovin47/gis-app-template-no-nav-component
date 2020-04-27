package com.gis.featureauth.presentation.ui.signin

import com.gis.repository.domain.entity.SignInCredsValidationResult
import com.gis.repository.domain.entity.User
import com.gis.repository.domain.entity.ValidationResult.Valid
import com.gis.repository.domain.interactors.*
import com.gis.utils.presentation.BaseViewModel
import com.gis.featureauth.presentation.ui.signin.SignInIntent.*
import com.gis.featureauth.presentation.ui.signin.SignInStateChange.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class SignInViewModel(
  private var goToSignUpScreen: (() -> Unit)?,
  private var showChooseUserDialog: (() -> Unit)?,
  private val signInCredsValidationUseCase: SignInCredsValidationUseCase,
  private val phoneValidationUseCase: PhoneValidationUseCase,
  private val signInWithCredsUseCase: SignInWithCredsUseCase,
  private val showFingerprintDialogAndCheckUsers: ShowFingerprintDialogAndCheckUsers,
  private val authWithFingerPrintUseCase: AuthWithFingerPrintUseCase,
  private val authWithFacebookUseCase: AuthWithFacebookUseCase,
  private val authWithTwitterUseCase: AuthWithTwitterUseCase,
  private val authWithInstagramUseCase: AuthWithInstagramUseCase,
  private val authWithGoogleUseCase: AuthWithGoogleUseCase
) : BaseViewModel<SignInState>() {

  override fun initState(): SignInState = SignInState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(listOf(

      intentStream.ofType(SignInWithCreds::class.java)
        .switchMap { event ->
          signInCredsValidationUseCase.validate(event.email, event.password)
            .switchMap { result ->
              if (result.emailValidation == Valid &&
                result.passwordValidation == Valid
              )
                signInWithCredsUseCase.execute(event.email, event.password)
                  .startWith(Observable.just(StartLoading))
                  .cast(SignInStateChange::class.java)
                  .onErrorResumeNext { e: Throwable -> handleError(e) }
                  .subscribeOn(Schedulers.io())
              else
                Observable.just(CredsValidationError(result))
            }
        },

      intentStream.ofType(SignInWithPhone::class.java)
        .switchMap { event ->
          phoneValidationUseCase.validate(event.phone)
            .switchMap { result ->
              if (result == Valid)
                Observable.just(ShowSmsCodeDialog, DontShowSmsCodeDialog)
              else
                Observable.just(PhoneValidationError(result))
            }
        },

      intentStream.ofType(GetUsersAvailableForFingerprintAuth::class.java)
        .switchMap { event ->
          showFingerprintDialogAndCheckUsers.execute(event.activity)
            .flatMap { users ->
              when {
                users.size > 1 -> Observable.just(SignInStateChange.ShowChooseUserDialog, DontShowChooseUserDialog)
                users.size == 1 -> Observable.just(AuthWithUser(users[0]), DontAuthWithUser)
                else -> Observable.just(Idle)
              }
            }
            .cast(SignInStateChange::class.java)
            .onErrorResumeNext { e: Throwable -> handleError(e) }
        },

      intentStream.ofType(SignInWithFingerprint::class.java)
        .switchMap { event ->
          authWithFingerPrintUseCase.execute(event.user)
            .toSingleDefault(Idle)
            .toObservable()
            .cast(SignInStateChange::class.java)
            .onErrorResumeNext { e: Throwable -> handleError(e) }
        },

      intentStream.ofType(SignInWithFacebook::class.java)
        .switchMap {
          authWithFacebookUseCase.execute()
            .toSingleDefault(FinishLoading)
            .toObservable()
            .cast(SignInStateChange::class.java)
            .startWith(Observable.just(StartLoading))
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
        },

      intentStream.ofType(SignInWithTwitter::class.java)
        .switchMap {
          authWithTwitterUseCase.execute()
            .toSingleDefault(FinishLoading)
            .toObservable()
            .cast(SignInStateChange::class.java)
            .startWith(Observable.just(StartLoading))
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
        },

      intentStream.ofType(SignInWithInstagram::class.java)
        .switchMap {
          authWithInstagramUseCase.execute()
            .toSingleDefault(FinishLoading)
            .toObservable()
            .cast(SignInStateChange::class.java)
            .startWith(Observable.just(StartLoading))
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
        },

      intentStream.ofType(SignInWithGoogle::class.java)
        .switchMap {
          authWithGoogleUseCase.execute()
            .toSingleDefault(FinishLoading)
            .toObservable()
            .cast(SignInStateChange::class.java)
            .startWith(Observable.just(StartLoading))
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
        },

      intentStream.ofType(SignInIntent.ShowChooseUserDialog::class.java)
        .doOnNext { showChooseUserDialog?.invoke() },

      intentStream.ofType(GoToSignUpScreen::class.java)
        .doOnNext { goToSignUpScreen?.invoke() }
    ))

  private fun handleError(error: Throwable) = Observable.just(Error(error), SignInStateChange.HideError)

  override fun reduceState(previousState: SignInState, stateChange: Any): SignInState =
    when (stateChange) {
      is StartLoading -> previousState.copy(
        loading = true,
        credsValidationResult = SignInCredsValidationResult(Valid, Valid),
        phoneValidationResult = Valid,
        error = null
      )

      is FinishLoading -> previousState.copy(loading = false)

      is ShowSmsCodeDialog -> previousState.copy(showSmsCodeDialog = true)

      is DontShowSmsCodeDialog -> previousState.copy(showSmsCodeDialog = false)

      is SignInStateChange.ShowChooseUserDialog -> previousState.copy(showChooseUserDialog = true)

      is DontShowChooseUserDialog -> previousState.copy(showChooseUserDialog = false)

      is AuthWithUser -> previousState.copy(authWithUser = true, fingerprintUser = stateChange.user)

      is DontAuthWithUser -> previousState.copy(authWithUser = false, fingerprintUser = User.EMPTY)

      is CredsValidationError -> previousState.copy(
        loading = false,
        credsValidationResult = stateChange.validationResult,
        phoneValidationResult = Valid
      )

      is PhoneValidationError -> previousState.copy(
        loading = false,
        credsValidationResult = SignInCredsValidationResult(Valid, Valid),
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
    goToSignUpScreen = null
    showChooseUserDialog = null
    super.onCleared()
  }
}