package com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo

import com.gis.repository.domain.entity.ValidationResult.Valid
import com.gis.repository.domain.interactors.*
import com.gis.utils.presentation.BaseViewModel
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo.ChangeUserInfoIntent.*
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo.ChangeUserInfoStateChange.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ChangeUserInfoViewModel(
  private val usernameValidationUseCase: UsernameValidationUseCase,
  private val emailValidationUseCase: EmailValidationUseCase,
  private val phoneValidationUseCase: PhoneValidationUseCase,
  private val observeUserUseCase: ObserveUserUseCase,
  private val updateUserUseCase: UpdateUserUseCase
) : BaseViewModel<ChangeUserInfoState>() {

  override fun initState(): ChangeUserInfoState =
    ChangeUserInfoState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(
      listOf(
        observeUserUseCase.execute()
          .map { user -> UserReceived(user) }
          .cast(ChangeUserInfoStateChange::class.java)
          .onErrorResumeNext { e: Throwable -> handleError(e) }
          .subscribeOn(Schedulers.io())
      )
    )

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(

        intentStream.ofType(ChangeUsername::class.java)
          .switchMap { event ->
            usernameValidationUseCase.validate(event.username)
              .switchMap { result ->
                if (result == Valid)
                  updateUserUseCase.execute(event.user.copy(username = event.username))
                    .toSingleDefault(CloseDialog)
                    .toObservable()
                    .cast(ChangeUserInfoStateChange::class.java)
                    .startWith(Observable.just(StartLoading))
                    .onErrorResumeNext { e: Throwable -> handleError(e) }
                    .subscribeOn(Schedulers.io())
                else
                  Observable.just(UserInfoValidationError(result))
              }
          },

        intentStream.ofType(ChangeEmail::class.java)
          .switchMap { event ->
            emailValidationUseCase.validate(event.email)
              .switchMap { result ->
                if (result == Valid)
                  updateUserUseCase.execute(event.user.copy(email = event.email))
                    .toSingleDefault(CloseDialog)
                    .toObservable()
                    .cast(ChangeUserInfoStateChange::class.java)
                    .startWith(Observable.just(StartLoading))
                    .onErrorResumeNext { e: Throwable -> handleError(e) }
                    .subscribeOn(Schedulers.io())
                else
                  Observable.just(UserInfoValidationError(result))
              }
          },

        intentStream.ofType(ChangePhone::class.java)
          .switchMap { event ->
            phoneValidationUseCase.validate(event.phone)
              .switchMap { result ->
                if (result == Valid)
                  updateUserUseCase.execute(event.user.copy(phone = event.phone))
                    .toSingleDefault(CloseDialog)
                    .toObservable()
                    .cast(ChangeUserInfoStateChange::class.java)
                    .startWith(Observable.just(StartLoading))
                    .onErrorResumeNext { e: Throwable -> handleError(e) }
                    .subscribeOn(Schedulers.io())
                else
                  Observable.just(UserInfoValidationError(result))
              }
          },

        intentStream.ofType(Cancel::class.java)
          .map { CloseDialog }
      )
    )

  private fun handleError(e: Throwable) =
    Observable.just(Error(e), HideError)

  override fun reduceState(previousState: ChangeUserInfoState, stateChange: Any): ChangeUserInfoState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true)

      is UserReceived -> previousState.copy(user = stateChange.user)

      is CloseDialog -> previousState.copy(closeDialog = true)

      is UserInfoValidationError -> previousState.copy(userInfoValidationError = stateChange.validationResult)

      is Error -> previousState.copy(loading = false, error = stateChange.error)

      is HideError -> previousState.copy(error = null)

      else -> previousState
    }
}