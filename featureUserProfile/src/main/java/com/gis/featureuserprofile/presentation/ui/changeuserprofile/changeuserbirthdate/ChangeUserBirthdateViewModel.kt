package com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserbirthdate

import com.gis.repository.domain.interactors.ObserveUserUseCase
import com.gis.repository.domain.interactors.UpdateUserUseCase
import com.gis.utils.presentation.BaseViewModel
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserbirthdate.ChangeUserBirthdateIntent.Cancel
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserbirthdate.ChangeUserBirthdateIntent.ChangeBirthdate
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserbirthdate.ChangeUserBirthdateStateChange.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ChangeUserBirthdateViewModel(
  private val observeUserUseCase: ObserveUserUseCase,
  private val updateUserUseCase: UpdateUserUseCase
) : BaseViewModel<ChangeUserBirthdateState>() {

  override fun initState(): ChangeUserBirthdateState = ChangeUserBirthdateState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(
      listOf(
        observeUserUseCase.execute()
          .map { user -> UserReceived(user) }
          .cast(ChangeUserBirthdateStateChange::class.java)
          .onErrorResumeNext { e: Throwable -> handleError(e) }
          .subscribeOn(Schedulers.io())
      )
    )

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(

        intentStream.ofType(ChangeBirthdate::class.java)
          .switchMap { event ->
            updateUserUseCase.execute(event.user)
              .toSingleDefault(CloseDialog)
              .toObservable()
              .cast(ChangeUserBirthdateStateChange::class.java)
              .startWith(Observable.just(StartLoading))
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
          },

        intentStream.ofType(Cancel::class.java)
          .map { CloseDialog }
      )
    )

  private fun handleError(e: Throwable) =
    Observable.just(Error(e), HideError)

  override fun reduceState(previousState: ChangeUserBirthdateState, stateChange: Any): ChangeUserBirthdateState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true)

      is UserReceived -> previousState.copy(user = stateChange.user)

      is CloseDialog -> previousState.copy(closeDialog = true)

      is Error -> previousState.copy(loading = false, error = stateChange.error)

      is HideError -> previousState.copy(error = null)

      else -> previousState
    }
}