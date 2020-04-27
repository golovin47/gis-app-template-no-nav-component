package com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserpassword

import com.gis.repository.domain.entity.ValidationResult.Valid
import com.gis.repository.domain.interactors.ChangePasswordUseCase
import com.gis.repository.domain.interactors.ChangePasswordValidationUseCase
import com.gis.repository.domain.interactors.ObserveUserUseCase
import com.gis.utils.presentation.BaseViewModel
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserpassword.ChangeUserPasswordIntent.Cancel
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserpassword.ChangeUserPasswordIntent.ChangePassword
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserpassword.ChangeUserPasswordStateChange.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ChangeUserPasswordViewModel(
  private val changePasswordValidationUseCase: ChangePasswordValidationUseCase,
  private val observeUserUseCase: ObserveUserUseCase,
  private val changePasswordUseCase: ChangePasswordUseCase
) : BaseViewModel<ChangeUserPasswordState>() {

  override fun initState(): ChangeUserPasswordState = ChangeUserPasswordState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(
      listOf(
        observeUserUseCase.execute()
          .map { user -> UserReceived(user) }
          .cast(ChangeUserPasswordStateChange::class.java)
          .onErrorResumeNext { e: Throwable -> handleError(e) }
          .subscribeOn(Schedulers.io())
      )
    )

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(

        intentStream.ofType(ChangePassword::class.java)
          .switchMap { event ->
            changePasswordValidationUseCase.validate(event.oldPassword, event.newPassword)
              .switchMap { result ->
                if (result.oldPasswordValidation == Valid &&
                  result.newPasswordValidation == Valid
                )
                  changePasswordUseCase.execute(event.oldPassword, event.newPassword)
                    .toSingleDefault(CloseDialog)
                    .toObservable()
                    .cast(ChangeUserPasswordStateChange::class.java)
                    .startWith(Observable.just(StartLoading))
                    .onErrorResumeNext { e: Throwable -> handleError(e) }
                    .subscribeOn(Schedulers.io())
                else
                  Observable.just(PasswordValidationError(result))
              }
          },

        intentStream.ofType(Cancel::class.java)
          .map { CloseDialog }
      )
    )

  private fun handleError(e: Throwable) =
    Observable.just(Error(e), HideError)

  override fun reduceState(previousState: ChangeUserPasswordState, stateChange: Any): ChangeUserPasswordState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true)

      is UserReceived -> previousState.copy(user = stateChange.user)

      is CloseDialog -> previousState.copy(closeDialog = true)

      is PasswordValidationError -> previousState.copy(passwordValidationResult = stateChange.changePasswordValidationResult)

      is Error -> previousState.copy(loading = false, error = stateChange.error)

      is HideError -> previousState.copy(error = null)

      else -> previousState
    }
}