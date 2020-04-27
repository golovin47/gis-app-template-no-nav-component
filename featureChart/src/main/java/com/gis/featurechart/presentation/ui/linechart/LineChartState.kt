package com.gis.featurechart.presentation.ui.linechart

import com.github.mikephil.charting.data.LineData

data class LineChartState(
  val loading: Boolean = false,
  val shouldAnimate: Boolean = true,
  val chartData: LineData = LineData(),
  val error: Throwable? = null
)


sealed class LineChartIntent {
  object ObserveChartData : LineChartIntent()
  object LoadOtherChartData : LineChartIntent()
}


sealed class LineChartStateChange {
  object Idle : LineChartStateChange()
  object StartLoading : LineChartStateChange()
  object DontAnimate : LineChartStateChange()
  class ChartDataReceived(val chartData: LineData) : LineChartStateChange()
  class Error(val error: Throwable) : LineChartStateChange()
  object HideError : LineChartStateChange()
}