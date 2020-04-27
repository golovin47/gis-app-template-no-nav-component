package com.gis.featureauthwithphone.presentation.ui.smscode

import com.gis.repository.domain.interactors.AuthWithPhoneUseCase
import com.gis.repository.domain.interactors.ObserveCountdownUseCase
import com.gis.repository.domain.interactors.StartCountdownUseCase
import com.gis.utils.presentation.BaseViewModel
import io.reactivex.Observable
import com.gis.featureauthwithphone.presentation.ui.smscode.SmsCodeStateChange.*
import com.gis.featureauthwithphone.presentation.ui.smscode.SmsCodeIntent.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SmsCodeViewModel(
  private var finsih: (() -> Unit)?,
  private val observeCountdownUseCase: ObserveCountdownUseCase,
  private val startCountdownUseCase: StartCountdownUseCase,
  private val authWithPhoneUseCase: AuthWithPhoneUseCase
) : BaseViewModel<SmsCodeState>() {

  override fun initState(): SmsCodeState = SmsCodeState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(
      listOf(
        observeCountdownUseCase.execute()
          .map { CountdownChanged(it) }
          .cast(SmsCodeStateChange::class.java)
          .onErrorResumeNext { e: Throwable -> handleError(e) }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread()),

        startCountdown()
      )
    )

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(

        intentStream.ofType(HideSmsCodeError::class.java)
          .map { HideError },

        intentStream.ofType(ResendCode::class.java)
          .switchMap { startCountdown() },

        intentStream.ofType(SendCode::class.java)
          .switchMap { event ->
            if (event.code.toInt() == 111111)
              Observable.just(Error(Throwable("Invalid code")))
            else
              authWithPhoneUseCase.execute(event.phone, event.code)
                .toSingleDefault(Idle)
                .toObservable()
                .cast(SmsCodeStateChange::class.java)
                .startWith(StartLoading)
                .onErrorResumeNext { e: Throwable -> handleError(e) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(GoBack::class.java)
          .doOnNext { finsih?.invoke() }
      )
    )

  private fun startCountdown() =
    startCountdownUseCase.execute()
      .toSingleDefault(Idle)
      .toObservable()
      .cast(SmsCodeStateChange::class.java)
      .onErrorResumeNext { e: Throwable -> handleError(e) }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())

  private fun handleError(e: Throwable) = Observable.just(Error(e), HideError)

  override fun reduceState(previousState: SmsCodeState, stateChange: Any): SmsCodeState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true, error = null)
      is CountdownChanged -> previousState.copy(counter = stateChange.count)
      is Error -> previousState.copy(loading = false, error = stateChange.error)
      is HideError -> previousState.copy(error = null)
      else -> previousState
    }

  override fun onCleared() {
    finsih = null
    super.onCleared()
  }
}