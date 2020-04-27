package com.gis.repository.domain.repository

import com.gis.repository.domain.entity.Gif
import com.gis.repository.domain.entity.GifCategory
import io.reactivex.Observable

interface GifRepository {

  fun getCategories(limit: Int?, offset: Int?, sort: String?): Observable<List<GifCategory>>

  fun getSubCategories(
    categoryEncodedName: String,
    limit: Int?,
    offset: Int?,
    sort: String?
  ): Observable<List<GifCategory>>

  fun getCategoryGifs(
    categoryEncodedName: String,
    subCategoryEncodedName: String,
    limit: Int?,
    offset: Int?,
    lang: String?
  ): Observable<List<Gif>>

  fun getPopularGifs(limit: Int?, offset: Int?): Observable<List<Gif>>

  fun search(query: String, limit: Int?, offset: Int?, lang: String?): Observable<List<Gif>>
}