package com.gis.featurechoosecountry.presentation.ui.choosecountry

import com.gis.repository.domain.entity.Country
import com.gis.repository.domain.interactors.ChooseCountryUseCase
import com.gis.repository.domain.interactors.GetCountriesListUseCase
import com.gis.repository.domain.interactors.SearchCountriesUseCase
import com.gis.utils.presentation.BaseViewModel
import com.gis.featurechoosecountry.presentation.ui.choosecountry.ChooseCountryIntent.*
import com.gis.featurechoosecountry.presentation.ui.choosecountry.ChooseCountryStateChange.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.gis.featurechoosecountry.presentation.ui.choosecountry.CountryListItem.*

class ChooseCountryViewModel(
  private var finish: (() -> Unit)?,
  private val getCountriesListUseCase: GetCountriesListUseCase,
  private val searchCountriesUseCase: SearchCountriesUseCase,
  private val chooseCountryUseCase: ChooseCountryUseCase
) : BaseViewModel<ChooseCountryState>() {

  override fun initState(): ChooseCountryState = ChooseCountryState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(listOf(
      getCountriesListUseCase.execute()
        .map { list -> CountriesReceived(list.map { it.toPresentation() }) }
        .cast(ChooseCountryStateChange::class.java)
        .onErrorResumeNext { e: Throwable -> handleError(e) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    ))

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> = Observable.merge(listOf(

    intentStream.ofType(SearchCountry::class.java)
      .switchMap { event ->
        if (event.query.isBlank()) {
          Observable.just(SearchCountriesReceived(emptyList()))
        } else
          searchCountriesUseCase.execute(event.query)
            .map { list ->
              SearchCountriesReceived(
                if (list.isEmpty()) {
                  listOf(EmptySearchItem)
                } else
                  list.map { it.toPresentation() })
            }
            .cast(ChooseCountryStateChange::class.java)
            .onErrorResumeNext { e: Throwable -> handleError(e) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
      },

    intentStream.ofType(ClearSearch::class.java)
      .map { SearchCountriesReceived(emptyList()) },

    intentStream.ofType(ChooseCountry::class.java)
      .switchMap { event ->
        chooseCountryUseCase.execute(event.name)
          .toSingleDefault(CountryChosen)
          .toObservable()
          .doOnNext { finish?.invoke() }
          .cast(ChooseCountryStateChange::class.java)
          .onErrorResumeNext { e: Throwable -> handleError(e) }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
      },

    intentStream.ofType(GoBack::class.java)
      .doOnNext { finish?.invoke() }
  ))

  private fun Country.toPresentation() = CountryItem(this)

  private fun handleError(e: Throwable) = Observable.just(Error(e), HideError)

  override fun reduceState(previousState: ChooseCountryState, stateChange: Any): ChooseCountryState =
    when (stateChange) {
      is CountriesReceived -> previousState.copy(countries = stateChange.countries)
      is SearchCountriesReceived -> previousState.copy(searchCountries = stateChange.countries)
      is Error -> previousState.copy(error = stateChange.error)
      is HideError -> previousState.copy(error = null)
      else -> previousState
    }

  override fun onCleared() {
    finish = null
    super.onCleared()
  }
}