package com.gis.repository.data.local.datasource

import android.content.res.AssetManager
import android.content.res.Resources
import com.gis.repository.domain.datasource.CountriesDataSource
import com.gis.repository.domain.entity.Country
import io.reactivex.Observable
import org.json.JSONObject
import java.nio.charset.Charset

class LocalCountriesDataSource(
  private val assetManager: AssetManager,
  private val resources: Resources
) : CountriesDataSource {

  private lateinit var countries: List<Country>

  override fun getCountries(): Observable<List<Country>> =
    Observable.fromCallable { getCountriesFromAsset() }
      .doOnNext { list -> countries = list }

  override fun searchCountries(query: String): Observable<List<Country>> =
    Observable.fromCallable {
      countries.filter { country ->
        country.name.toLowerCase().startsWith(query.toLowerCase())
      }
    }

  private fun getCountriesFromAsset(): List<Country> {
    val inputStream = assetManager.open("country_codes.json")
    val size = inputStream.available()
    val buffer = ByteArray(size) { Byte.MIN_VALUE }
    inputStream.read(buffer)
    inputStream.close()
    val jsonString = String(buffer, Charset.forName("UTF-8"))
    val jsonObject = JSONObject(jsonString)
    val jsonCountriesArray = jsonObject.getJSONArray("countries")
    val countries = mutableListOf<Country>()

    for (i in 0 until jsonCountriesArray.length()) {
      val jsonCountry = jsonCountriesArray.getJSONObject(i)
      val name = jsonCountry.getString("name")
      val code = jsonCountry.getString("code")
      val resName = "flag_of_${name.toLowerCase().replace(" ", "_")}"
      val flagResId = resources.getIdentifier(resName, "drawable", "com.gis.apptemplateandroid")
      if (flagResId != 0) {
        val country = Country(name = name, code = code, flagResId = flagResId)
        countries.add(country)
      }
    }

    return countries
  }
}