package com.gis.featureauthwithphone.presentation.ui.enterphone

import com.gis.repository.domain.entity.ValidationResult.Valid
import com.gis.repository.domain.interactors.GetChosenCountryUseCase
import com.gis.repository.domain.interactors.PhoneFormatUseCase
import com.gis.repository.domain.interactors.PhoneValidationUseCase
import com.gis.utils.presentation.BaseViewModel
import com.gis.featureauthwithphone.presentation.ui.enterphone.EnterPhoneIntent.*
import com.gis.featureauthwithphone.presentation.ui.enterphone.EnterPhoneStateChange.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class EnterPhoneViewModel(
  private var goToChooseCountry: (() -> Unit)?,
  private var goToSmsCode: ((String) -> Unit)?,
  private val getChosenCountryUseCase: GetChosenCountryUseCase,
  private val phoneValidationUseCase: PhoneValidationUseCase,
  private val phoneFormatUseCase: PhoneFormatUseCase
) : BaseViewModel<EnterPhoneState>() {

  override fun initState(): EnterPhoneState = EnterPhoneState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(

        intentStream.ofType(ObserveCurrentCountry::class.java)
          .switchMap { event ->
            getChosenCountryUseCase.execute()
              .map { CountryReceived(it) }
              .cast(EnterPhoneStateChange::class.java)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          },

        intentStream.ofType(ChooseCountry::class.java)
          .doOnNext { goToChooseCountry?.invoke() },

        intentStream.ofType(PhoneInputChanged::class.java)
          .switchMap { event ->
            phoneValidationUseCase.validate(event.phone)
              .flatMap { result ->
                if (result !is Valid)
                  Observable.just(PhoneValidated(result, event.phone))
                else
                  phoneFormatUseCase.execute(event.phone)
                    .map { formattedPhone -> PhoneValidated(result, formattedPhone) }
              }
          },

        intentStream.ofType(SendPhone::class.java)
          .doOnNext { event -> goToSmsCode?.invoke(event.phone) }
      )
    )

  private fun handleError(e: Throwable) = Observable.just(Error(e), HideError)

  override fun reduceState(previousState: EnterPhoneState, stateChange: Any): EnterPhoneState =
    when (stateChange) {
      is CountryReceived -> previousState.copy(chosenCountry = stateChange.country)
      is PhoneValidated -> previousState.copy(
        phoneValidationResult = stateChange.validationResult,
        formattedPhone = stateChange.phoneFormatted
      )
      is Error -> previousState.copy(loading = false, error = stateChange.error)
      is HideError -> previousState.copy(error = null)
      else -> previousState
    }

  override fun onCleared() {
    goToChooseCountry = null
    goToSmsCode = null
    super.onCleared()
  }
}