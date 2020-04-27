package com.gis.featurechoosecountry.presentation.ui.choosecountry

import com.gis.repository.domain.entity.Country

data class ChooseCountryState(
  val countries: List<CountryListItem> = emptyList(),
  val searchCountries: List<CountryListItem> = emptyList(),
  val error: Throwable? = null
)


sealed class ChooseCountryIntent {
  class ChooseCountry(val name: String) : ChooseCountryIntent()
  class SearchCountry(val query: String) : ChooseCountryIntent()
  object ClearSearch : ChooseCountryIntent()
  object GoBack : ChooseCountryIntent()
}


sealed class ChooseCountryStateChange {
  class CountriesReceived(val countries: List<CountryListItem>) : ChooseCountryStateChange()
  class SearchCountriesReceived(val countries: List<CountryListItem>) : ChooseCountryStateChange()
  object CountryChosen : ChooseCountryStateChange()
  class Error(val error: Throwable) : ChooseCountryStateChange()
  object HideError : ChooseCountryStateChange()
}


sealed class CountryListItem {
  class CountryItem(val country: Country) : CountryListItem()
  object EmptySearchItem : CountryListItem()
}