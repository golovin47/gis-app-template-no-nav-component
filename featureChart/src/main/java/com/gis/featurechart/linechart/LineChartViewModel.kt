package com.gis.featurechart.linechart

import com.gis.repository.domain.entity.ChartDataEntry
import com.gis.repository.domain.interactors.LoadOtherChartDataUseCase
import com.gis.repository.domain.interactors.ObserveChartDataUseCase
import com.gis.utils.presentation.BaseViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import io.reactivex.Observable
import com.gis.featurechart.linechart.LineChartIntent.*
import com.gis.featurechart.linechart.LineChartStateChange.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LineChartViewModel(
  private val observeChartDataUseCase: ObserveChartDataUseCase,
  private val loadOtherChartDataUseCase: LoadOtherChartDataUseCase
) : BaseViewModel<LineChartState>() {

  override fun initState(): LineChartState = LineChartState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(listOf(
      observeChartDataUseCase.execute()
        .flatMap { dataSet -> Observable.just(ChartDataReceived(chartDataSetToPresentation(dataSet)), DontAnimate) }
        .cast(LineChartStateChange::class.java)
        .onErrorResumeNext { e: Throwable -> handleError(e) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
    ))

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(

        intentStream.ofType(LoadOtherChartData::class.java)
          .switchMap {
            loadOtherChartDataUseCase.execute()
              .toSingleDefault(Idle)
              .toObservable()
              .cast(LineChartStateChange::class.java)
              .onErrorResumeNext { e: Throwable -> handleError(e) }
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
          }

      )
    )

  private fun chartDataSetToPresentation(dataSet: List<List<ChartDataEntry>>): LineData {
    val dataSets: MutableList<LineDataSet> = mutableListOf()
    for (list in dataSet) {
      val lineDataSet = LineDataSet(list.map { it.toPresentation() }, "").apply { valueTextSize = 14f }
      dataSets.add(lineDataSet)
    }
    return LineData(dataSets as List<ILineDataSet>?)
  }

  private fun ChartDataEntry.toPresentation() = Entry(x, y)

  private fun handleError(e: Throwable) = Observable.just(Error(e), HideError)

  override fun reduceState(previousState: LineChartState, stateChange: Any): LineChartState =
    when (stateChange) {
      is ChartDataReceived -> previousState.copy(
        chartData = stateChange.chartData,
        shouldAnimate = true
      )
      is DontAnimate -> previousState.copy(shouldAnimate = false)
      is Error -> previousState.copy(error = stateChange.error)
      is HideError -> previousState.copy(error = null)
      else -> previousState
    }
}