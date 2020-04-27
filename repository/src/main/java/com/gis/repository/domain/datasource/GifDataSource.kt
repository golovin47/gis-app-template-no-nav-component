package com.gis.repository.domain.datasource

import com.gis.repository.domain.entity.Gif
import com.gis.repository.domain.entity.GifCategory
import io.reactivex.Observable

interface GifDataSource {

  fun getCategories(limit: Int? = null, offset: Int? = null, sort: String? = null): Observable<List<GifCategory>>

  fun getSubCategories(
    categoryEncodedName: String,
    limit: Int? = null,
    offset: Int? = null,
    sort: String? = null
  ): Observable<List<GifCategory>>

  fun getCategoryGifs(
    categoryEncodedName: String,
    subCategoryEncodedName: String,
    limit: Int? = null,
    offset: Int?,
    lang: String?
  ): Observable<List<Gif>>

  fun getPopularGifs(limit: Int? = null, offset: Int?): Observable<List<Gif>>

  fun search(query: String, limit: Int? = null, offset: Int? = null, lang: String? = null): Observable<List<Gif>>
}