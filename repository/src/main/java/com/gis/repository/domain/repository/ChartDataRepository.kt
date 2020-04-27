package com.gis.repository.domain.repository

import com.gis.repository.domain.entity.ChartDataEntry
import io.reactivex.Completable
import io.reactivex.Observable

interface ChartDataRepository {

  fun observeChartData(): Observable<List<List<ChartDataEntry>>>

  fun loadOtherChartData(): Completable
}