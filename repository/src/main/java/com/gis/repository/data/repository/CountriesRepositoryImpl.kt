package com.gis.repository.data.repository

import com.gis.repository.domain.datasource.CountriesDataSource
import com.gis.repository.domain.entity.Country
import com.gis.repository.domain.repository.CountriesRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class CountriesRepositoryImpl(private val localCountriesDataSource: CountriesDataSource) : CountriesRepository {

  private val countriesPublisher = BehaviorSubject.createDefault<List<Country>>(emptyList())
  private val chosenCountryPublisher = BehaviorSubject.createDefault<Country>(Country.DEFAULT)

  override fun getCountries(): Observable<List<Country>> = countriesPublisher.switchMap { list ->
    if (list.isEmpty()) {
      localCountriesDataSource.getCountries().doOnNext { countriesPublisher.onNext(it) }
    } else {
      Observable.just(list)
    }
  }

  override fun searchCountries(query: String): Observable<List<Country>> =
    localCountriesDataSource.searchCountries(query)

  override fun getCurrentCountry(): Observable<Country> = chosenCountryPublisher

  override fun chooseCountry(name: String): Completable = Completable.fromAction {
    val chosenCountry = countriesPublisher.value!!.find { country -> country.name == name }!!
    chosenCountryPublisher.onNext(chosenCountry)
  }
}