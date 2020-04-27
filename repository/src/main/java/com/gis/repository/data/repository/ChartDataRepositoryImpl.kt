package com.gis.repository.data.repository

import com.gis.repository.domain.datasource.ChartDataDataSource
import com.gis.repository.domain.entity.ChartDataEntry
import com.gis.repository.domain.repository.ChartDataRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class ChartDataRepositoryImpl(private val localChartDataDataSource: ChartDataDataSource) : ChartDataRepository {

  private val chartDataPublisher = BehaviorSubject.createDefault<List<List<ChartDataEntry>>>(emptyList())

  override fun observeChartData(): Observable<List<List<ChartDataEntry>>> = chartDataPublisher.switchMap { entries ->
    if (entries.isEmpty())
      localChartDataDataSource.getChartData()
        .doOnNext { chartDataPublisher.onNext(it) }
        .switchMap { chartDataPublisher }
    else
      chartDataPublisher
  }

  override fun loadOtherChartData(): Completable =
    Completable.fromObservable(
      localChartDataDataSource.getOtherChartData()
        .doOnNext { chartDataPublisher.onNext(it) }
    )
}