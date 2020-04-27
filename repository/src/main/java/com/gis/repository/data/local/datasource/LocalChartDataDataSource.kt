package com.gis.repository.data.local.datasource

import com.gis.repository.domain.datasource.ChartDataDataSource
import com.gis.repository.domain.entity.ChartDataEntry
import io.reactivex.Observable

class LocalChartDataDataSource : ChartDataDataSource {

  private var first = true

  override fun getChartData(): Observable<List<List<ChartDataEntry>>> =
    Observable.fromCallable { listOf(createDataSet()) }

  override fun getOtherChartData(): Observable<List<List<ChartDataEntry>>> =
    Observable.fromCallable { listOf(createDataSet()) }

  private fun createDataSet(): List<ChartDataEntry> {
    first = !first
    val dataSet = mutableListOf<ChartDataEntry>()
    if (first) {
      dataSet.add(ChartDataEntry(1f, 1f))
      dataSet.add(ChartDataEntry(2f, 0f))
      dataSet.add(ChartDataEntry(3f, 2f))
      dataSet.add(ChartDataEntry(4f, 1f))
      dataSet.add(ChartDataEntry(5f, 3f))
      dataSet.add(ChartDataEntry(6f, 2f))
      dataSet.add(ChartDataEntry(7f, 4f))
      dataSet.add(ChartDataEntry(8f, 3f))
      dataSet.add(ChartDataEntry(9f, 5f))
      dataSet.add(ChartDataEntry(10f, 9f))
    } else {
      dataSet.add(ChartDataEntry(1f, 9f))
      dataSet.add(ChartDataEntry(2f, 5f))
      dataSet.add(ChartDataEntry(3f, 3f))
      dataSet.add(ChartDataEntry(4f, 4f))
      dataSet.add(ChartDataEntry(5f, 2f))
      dataSet.add(ChartDataEntry(6f, 3f))
      dataSet.add(ChartDataEntry(7f, 1f))
      dataSet.add(ChartDataEntry(8f, 2f))
      dataSet.add(ChartDataEntry(9f, 0f))
      dataSet.add(ChartDataEntry(10f, 1f))
    }
    return dataSet
  }
}