package com.gis.featureauth.presentation.ui.smscode

import com.gis.repository.domain.interactors.AuthWithPhoneUseCase
import com.gis.utils.presentation.BaseViewModel
import com.gis.featureauth.presentation.ui.smscode.SmsCodeIntent.Cancel
import com.gis.featureauth.presentation.ui.smscode.SmsCodeIntent.Send
import com.gis.featureauth.presentation.ui.smscode.SmsCodeStateChange.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class SmsCodeDialogViewModel(private val authWithPhoneUseCase: AuthWithPhoneUseCase) : BaseViewModel<SmsCodeState>() {

  override fun initState(): SmsCodeState = SmsCodeState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(

        intentStream.ofType(Send::class.java)
          .switchMap { event ->
            authWithPhoneUseCase.execute(phone = event.phone, code = event.code)
              .toSingleDefault(CloseDialog)
              .toObservable()
              .cast(SmsCodeStateChange::class.java)
              .startWith(Observable.just(StartLoading))
              .onErrorResumeNext { e: Throwable -> Observable.just(Error(e), HideError) }
              .subscribeOn(Schedulers.io())
          },

        intentStream.ofType(Cancel::class.java)
          .map { CloseDialog }
      )
    )

  override fun reduceState(previousState: SmsCodeState, stateChange: Any): SmsCodeState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true, error = null)

      is CloseDialog -> previousState.copy(closeDialog = true)

      is Error -> previousState.copy(loading = false, error = stateChange.error)

      is HideError -> previousState.copy(error = null)

      else -> previousState
    }
}