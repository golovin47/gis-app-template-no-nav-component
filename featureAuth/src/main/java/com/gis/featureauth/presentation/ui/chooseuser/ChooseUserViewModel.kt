package com.gis.featureauth.presentation.ui.chooseuser

import com.gis.repository.domain.entity.User
import com.gis.repository.domain.interactors.AuthWithFingerPrintUseCase
import com.gis.repository.domain.interactors.GetFingerprintUsers
import com.gis.repository.domain.interactors.ShowFingerprintDialogAndCheckUsers
import com.gis.utils.presentation.BaseViewModel
import io.reactivex.Observable
import com.gis.featureauth.presentation.ui.chooseuser.ChooseUserIntent.*
import com.gis.featureauth.presentation.ui.chooseuser.ChooseUserStateChange.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.gis.featureauth.presentation.ui.chooseuser.ChooseUserListItem.*

class ChooseUserViewModel(
  private val getFingerprintUsers: GetFingerprintUsers,
  private val authWithFingerPrintUseCase: AuthWithFingerPrintUseCase
) : BaseViewModel<ChooseUserState>() {

  override fun initState(): ChooseUserState = ChooseUserState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(
        intentStream.ofType(GetUsers::class.java)
          .switchMap { event ->
            getFingerprintUsers.execute()
              .map { users -> UsersReceived(users) }
              .cast(ChooseUserStateChange::class.java)
              .startWith(StartLoading)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(AuthWithUser::class.java)
          .switchMap { event ->
            authWithFingerPrintUseCase.execute(event.user)
              .toSingleDefault(Idle)
              .toObservable()
              .cast(ChooseUserStateChange::class.java)
              .startWith(StartLoading)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          }
      )
    )

  private fun handleError(e: Throwable) = Observable.just(Error(e), HideError)

  override fun reduceState(previousState: ChooseUserState, stateChange: Any): ChooseUserState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = false, error = null)
      is UsersReceived -> previousState.copy(
        loading = false,
        users = stateChange.users.map { it.toListItem() },
        error = null
      )
      is Error -> previousState.copy(loading = false, error = stateChange.error)
      is HideError -> previousState.copy(error = null)
      else -> previousState
    }

  private fun User.toListItem() = UserItem(this)
}