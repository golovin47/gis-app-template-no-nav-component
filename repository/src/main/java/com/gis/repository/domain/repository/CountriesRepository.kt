package com.gis.repository.domain.repository

import com.gis.repository.domain.entity.Country
import io.reactivex.Completable
import io.reactivex.Observable

interface CountriesRepository {

  fun getCountries(): Observable<List<Country>>

  fun searchCountries(query: String): Observable<List<Country>>

  fun getCurrentCountry(): Observable<Country>

  fun chooseCountry(name: String): Completable
}