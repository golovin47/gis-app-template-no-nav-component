package com.gis.featureuserprofile.presentation.ui.userimagepreview

import com.gis.repository.domain.interactors.ObserveUserUseCase
import com.gis.repository.domain.interactors.UpdateUserUseCase
import com.gis.utils.presentation.BaseViewModel
import com.gis.featureuserprofile.presentation.ui.userimagepreview.UserImagePreviewIntent.Cancel
import com.gis.featureuserprofile.presentation.ui.userimagepreview.UserImagePreviewIntent.UpdateAvatar
import com.gis.featureuserprofile.presentation.ui.userimagepreview.UserImagePreviewStateChange.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class UserImagePreviewViewModel(
  private val observeUserUseCase: ObserveUserUseCase,
  private val updateUserUseCase: UpdateUserUseCase,
  private var goBack: (() -> Unit)?
) : BaseViewModel<UserImagePreviewState>() {

  override fun initState(): UserImagePreviewState = UserImagePreviewState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(listOf(
      observeUserUseCase.execute()
        .map { UserReceived(it) }
    ))

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(listOf(

      intentStream.ofType(UpdateAvatar::class.java)
        .switchMap { event ->
          updateUserUseCase.execute(event.user)
            .toSingleDefault(Success)
            .toObservable()
            .cast(UserImagePreviewStateChange::class.java)
            .doOnNext { if (it is Success) goBack?.invoke() }
            .startWith(StartLoading)
            .onErrorResumeNext { e: Throwable -> Observable.just(Error(e), HideError) }
            .subscribeOn(Schedulers.io())
        },

      intentStream.ofType(Cancel::class.java)
        .doOnNext { goBack?.invoke() }
    ))

  override fun reduceState(previousStateUser: UserImagePreviewState, stateChange: Any): UserImagePreviewState =
    when (stateChange) {
      is StartLoading -> previousStateUser.copy(loading = false)

      is UserReceived -> previousStateUser.copy(user = stateChange.user)

      is Error -> previousStateUser.copy(error = stateChange.error)

      is HideError -> previousStateUser.copy(error = null)

      else -> previousStateUser
    }

  override fun onCleared() {
    goBack = null
    super.onCleared()
  }
}