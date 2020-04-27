package com.gis.repository.data.repository

import com.gis.repository.domain.datasource.GifDataSource
import com.gis.repository.domain.entity.Gif
import com.gis.repository.domain.entity.GifCategory
import com.gis.repository.domain.repository.GifRepository
import io.reactivex.Observable

class GifRepositoryImpl(private val remoteGifDataSource: GifDataSource) : GifRepository {

  override fun getCategories(limit: Int?, offset: Int?, sort: String?): Observable<List<GifCategory>> =
    remoteGifDataSource.getCategories(limit, offset, sort)

  override fun getSubCategories(categoryEncodedName: String, limit: Int?, offset: Int?, sort: String?) =
    remoteGifDataSource.getSubCategories(categoryEncodedName, limit, offset, sort)

  override fun getCategoryGifs(
    categoryEncodedName: String,
    subCategoryEncodedName: String,
    limit: Int?,
    offset: Int?,
    lang: String?
  ): Observable<List<Gif>> =
    remoteGifDataSource.getCategoryGifs(categoryEncodedName, subCategoryEncodedName, limit, offset, lang)

  override fun getPopularGifs(limit: Int?, offset: Int?): Observable<List<Gif>> =
    remoteGifDataSource.getPopularGifs(limit, offset)

  override fun search(query: String, limit: Int?, offset: Int?, lang: String?): Observable<List<Gif>> =
    remoteGifDataSource.search(query, limit, offset, lang)
}