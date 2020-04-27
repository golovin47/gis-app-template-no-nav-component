package com.gis.featureuserprofile.presentation.ui.userprofile

import com.gis.repository.domain.interactors.EnableFingerprintAuthUseCase
import com.gis.repository.domain.interactors.ObserveUserUseCase
import com.gis.repository.domain.interactors.SignOutUseCase
import com.gis.utils.presentation.BaseViewModel
import com.gis.featureuserprofile.presentation.ui.userprofile.UserProfileIntent.*
import com.gis.featureuserprofile.presentation.ui.userprofile.UserProfileStateChange.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserProfileViewModel(
  private val observeUserUseCase: ObserveUserUseCase,
  private val enableFingerprintAuthUseCase: EnableFingerprintAuthUseCase,
  private val signOutUseCase: SignOutUseCase,
  private var showChangeUsernameDialog: (() -> Unit)?,
  private var showChangeEmailDialog: (() -> Unit)?,
  private var showChangePhoneDialog: (() -> Unit)?,
  private var showChangeBirthdateDialog: ((Int, Int, Int) -> Unit)?,
  private var showChangePasswordDialog: (() -> Unit)?,
  private var showExtraPermissionsDialog: (() -> Unit)?,
  private var showImageSourceDialog: (() -> Unit)?
) : BaseViewModel<UserProfileState>() {

  override fun initState(): UserProfileState = UserProfileState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(

        intentStream.ofType(StartObserveUser::class.java)
          .switchMap { event ->
            observeUserUseCase.execute()
              .map { user -> UserReceived(user) }
          },

        intentStream.ofType(EnableFingerprint::class.java)
          .switchMap { event ->
            enableFingerprintAuthUseCase.execute(event.enable)
              .toSingleDefault(Idle)
              .toObservable()
              .cast(UserProfileStateChange::class.java)
              .onErrorResumeNext { e: Throwable -> Observable.just(Error(e), HideError) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(SignOut::class.java)
          .switchMap { event ->
            signOutUseCase.execute()
              .toSingleDefault(Idle)
              .toObservable()
              .cast(UserProfileStateChange::class.java)
              .onErrorResumeNext { e: Throwable -> Observable.just(Error(e), HideError) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(ExpandCtbl::class.java)
          .map { CtblExpanded },

        intentStream.ofType(CollapseCtbl::class.java)
          .map { CtblCollapsed },

        intentStream.ofType(ChangeUsername::class.java)
          .doOnNext { showChangeUsernameDialog?.invoke() },

        intentStream.ofType(ChangeEmail::class.java)
          .doOnNext { showChangeEmailDialog?.invoke() },

        intentStream.ofType(ChangePhone::class.java)
          .doOnNext { showChangePhoneDialog?.invoke() },

        intentStream.ofType(ChangeBirthDate::class.java)
          .doOnNext { event ->
            showChangeBirthdateDialog?.invoke(event.year, event.month, event.day)
          },

        intentStream.ofType(ChangePassword::class.java)
          .doOnNext { showChangePasswordDialog?.invoke() },

        intentStream.ofType(ChangeAvatarUrl::class.java)
          .doOnNext { showImageSourceDialog?.invoke() }
      )
    )

  override fun reduceState(previousState: UserProfileState, stateChange: Any): UserProfileState =
    when (stateChange) {
      is UserReceived -> previousState.copy(user = stateChange.user)

      is CtblExpanded -> previousState.copy(ctblExpanded = true)

      is CtblCollapsed -> previousState.copy(ctblExpanded = false)

      else -> previousState
    }

  override fun onCleared() {
    showChangeUsernameDialog = null
    showChangeEmailDialog = null
    showChangePhoneDialog = null
    showChangeBirthdateDialog = null
    showChangePasswordDialog = null
    showExtraPermissionsDialog = null
    showImageSourceDialog = null
    super.onCleared()
  }
}