package com.gis.repository.domain.interactors

import com.gis.repository.domain.repository.CountriesRepository
import com.gis.repository.domain.service.CountdownService

class GetCountriesListUseCase(private val countriesRepository: CountriesRepository) {
  fun execute() = countriesRepository.getCountries()
}


class SearchCountriesUseCase(private val countriesRepository: CountriesRepository) {
  fun execute(query: String) = countriesRepository.searchCountries(query)
}


class GetChosenCountryUseCase(private val countriesRepository: CountriesRepository) {
  fun execute() = countriesRepository.getCurrentCountry()
}


class ChooseCountryUseCase(private val countriesRepository: CountriesRepository) {
  fun execute(name: String) = countriesRepository.chooseCountry(name)
}


class ObserveCountdownUseCase(private val countdownService: CountdownService) {
  fun execute() = countdownService.observeCountdown()
}


class StartCountdownUseCase(private val countdownService: CountdownService) {
  fun execute() = countdownService.startCountdown()
}


class FinishCountdownUseCase(private val countdownService: CountdownService) {
  fun execute() = countdownService.finsihCountdown()
}