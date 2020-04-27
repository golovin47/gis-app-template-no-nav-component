package com.gis.repository.domain.datasource

import com.gis.repository.domain.entity.ChartDataEntry
import io.reactivex.Observable

interface ChartDataDataSource {

  fun getChartData(): Observable<List<List<ChartDataEntry>>>

  fun getOtherChartData(): Observable<List<List<ChartDataEntry>>>
}