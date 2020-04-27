package com.gis.repository.domain.entity

data class GifCategory(
  val name: String,
  val nameEncoded: String,
  val gif: Gif,
  val subCategories: List<GifCategory>
) {
  companion object {
    val EMPTY = GifCategory("", "", Gif.EMPTY, emptyList())
  }
}


data class Gif(
  val id: String,
  val title: String,
  val url: String
) {
  companion object {
    val EMPTY = Gif("", "", "")
  }
}