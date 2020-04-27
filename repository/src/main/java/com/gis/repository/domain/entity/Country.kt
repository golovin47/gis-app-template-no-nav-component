package com.gis.repository.domain.entity

import com.gis.repository.R

data class Country(
  val name: String,
  val code: String,
  val flagResId: Int
) {
  companion object {
    val DEFAULT = Country("Russia", "+7", R.drawable.flag_of_russia)
  }
}