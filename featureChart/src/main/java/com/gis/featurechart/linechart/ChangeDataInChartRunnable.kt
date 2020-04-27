package com.gis.featurechart.linechart

import android.os.Handler
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineDataSet

class ChangeDataInChartRunnable(
  private var iterations: Int,
  private val iterationDuration: Long,
  private val dx: Array<Float>,
  private val dy: Array<Float>,
  private val dataSet: LineDataSet,
  private val chartView: LineChart,
  private val handler: Handler
) : Runnable {

  private var counter = 0
  private val xOffset = Array(dx.size) { 0f }

  override fun run() {
    for (e in dataSet.values) {
      xOffset[counter % dx.size] += dx[counter % dx.size]
      if (xOffset[counter % dx.size] >= 1 || xOffset[counter % dx.size] <= -1) {
        e.x = (e.x + xOffset[counter % dx.size].toInt())
        xOffset[counter % dx.size] -= xOffset[counter % dx.size].toInt().toFloat()
      }
      e.y = e.y + dy[counter % dy.size]
      counter++
    }
    chartView.invalidate()
    iterations--
    if (iterations > 0) {
      handler.postDelayed(this, iterationDuration)
    }
  }
}