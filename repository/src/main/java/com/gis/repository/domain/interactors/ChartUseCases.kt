package com.gis.repository.domain.interactors

import com.gis.repository.domain.repository.ChartDataRepository

class ObserveChartDataUseCase(private val chartDataRepository: ChartDataRepository) {
  fun execute() = chartDataRepository.observeChartData()
}


class LoadOtherChartDataUseCase(private val chartDataRepository: ChartDataRepository) {
  fun execute() = chartDataRepository.loadOtherChartData()
}