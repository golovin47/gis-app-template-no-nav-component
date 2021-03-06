package com.gis.featurechart.presentation.ui.linechart

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gis.utils.presentation.BaseView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featurefriends.databinding.FragmentLineChartBinding
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.gis.featurechart.presentation.ui.linechart.LineChartIntent.*
import com.gis.featurefriends.R
import com.gis.utils.presentation.BaseMviFragment
import java.util.concurrent.TimeUnit

class LineChartFragment :
  BaseMviFragment<LineChartState, FragmentLineChartBinding, LineChartViewModel>() {

  override val layoutId: Int = R.layout.fragment_line_chart
  override val viewModel: LineChartViewModel by viewModel()

  private val animHandler = Handler()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    initChartView()
    return binding!!.root
  }

  private fun initChartView() {
    with(binding!!.lineChart) {
      description.text = ""
      legend.isEnabled = false
    }
  }

  override fun userIntents(): Observable<Any> =
    Observable.merge(
      listOf(
        RxView.clicks(binding!!.btnChangeDataSet)
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
          .map { LoadOtherChartData }
      )
    )

  override fun render(state: LineChartState) {
    changeDataInChart(state)
  }

  private fun changeDataInChart(state: LineChartState) {
    if (binding!!.lineChart.data != state.chartData) {
      if (binding!!.lineChart.data == null || binding!!.lineChart.data.dataSetCount == 0) {
        binding!!.lineChart.data = state.chartData
        binding!!.lineChart.invalidate()
      } else {
        if (state.shouldAnimate)
          binding!!.lineChart.animateChangeDataInChart(
            binding!!.lineChart.data.dataSets[0] as LineDataSet,
            state.chartData.dataSets[0] as LineDataSet,
            1000L
          )
      }
    }
  }

  private fun LineChart.animateChangeDataInChart(
    prevData: LineDataSet,
    newData: LineDataSet,
    duration: Long
  ) {
    val dy = Array(prevData.entryCount) { 0f }
    val dx = Array(prevData.entryCount) { 0f }
    val iterations = (duration.toInt() / 1000) * 30
    val iterationDuration = duration / iterations
    val endEntries = newData.values
    val startEntries = prevData.values
    for (i in 0 until dx.size) {
      dx[i] = (endEntries[i].x - startEntries[i].x) / iterations
      dy[i] = (endEntries[i].y - startEntries[i].y) / iterations
    }
    val animationRunnable =
      ChangeDataInChartRunnable(iterations, iterationDuration, dx, dy, prevData, this, animHandler)
    animHandler.post(animationRunnable)
  }
}