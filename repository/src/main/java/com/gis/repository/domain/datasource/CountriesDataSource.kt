package com.gis.repository.domain.datasource

import com.gis.repository.domain.entity.Country
import io.reactivex.Observable

interface CountriesDataSource {

  fun getCountries(): Observable<List<Country>>

  fun searchCountries(query: String): Observable<List<Country>>
}