package com.gis.repository.data.remote.datasource

import com.giphy.sdk.core.models.Category
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.LangType
import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.core.models.enums.RatingType
import com.giphy.sdk.core.network.api.GPHApiClient
import com.gis.repository.domain.datasource.GifDataSource
import com.gis.repository.domain.entity.Gif
import com.gis.repository.domain.entity.GifCategory
import io.reactivex.Observable

class RemoteGifDataSource : GifDataSource {

  private val gifApi = GPHApiClient("EEHBBVkbH7m2D2pKhIUpqysaaRJZuQr2")

  override fun getCategories(limit: Int?, offset: Int?, sort: String?): Observable<List<GifCategory>> =
    Observable.create { emitter ->
      gifApi.categoriesForGifs(limit ?: 25, offset, sort) { result, error ->
        if (error == null)
          emitter.onNext(result.data.map { it.toDomain() })
        else
          emitter.onError(error)
      }
    }

  override fun getSubCategories(
    categoryEncodedName: String,
    limit: Int?,
    offset: Int?,
    sort: String?
  ): Observable<List<GifCategory>> =
    Observable.create { emitter ->
      gifApi.subCategoriesForGifs(categoryEncodedName, limit ?: 25, offset, sort) { result, error ->
        if (error == null)
          emitter.onNext(result.data.map { it.toDomain() })
        else
          emitter.onError(error)
      }
    }

  override fun getCategoryGifs(
    categoryEncodedName: String,
    subCategoryEncodedName: String,
    limit: Int?,
    offset: Int?,
    lang: String?
  ): Observable<List<Gif>> =
    Observable.create { emitter ->
      gifApi.gifsByCategory(
        categoryEncodedName,
        subCategoryEncodedName,
        limit ?: 25,
        offset,
        RatingType.unrated,
        LangType.english
      ) { result, error ->
        if (error == null)
          emitter.onNext(result.data.map { it.toDomain() })
        else
          emitter.onError(error)
      }
    }

  override fun getPopularGifs(limit: Int?, offset: Int?): Observable<List<Gif>> =
    Observable.create { emitter ->
      gifApi.trending(MediaType.gif, limit ?: 25, offset, RatingType.unrated) { result, error ->
        if (error == null)
          emitter.onNext(result.data.map { it.toDomain() })
        else
          emitter.onError(error)
      }
    }

  override fun search(query: String, limit: Int?, offset: Int?, lang: String?): Observable<List<Gif>> =
    Observable.create { emitter ->
      gifApi.search(
        query,
        MediaType.gif,
        limit ?: 25,
        offset,
        null,
        LangType.english
      ) { result, error ->
        if (error == null)
          emitter.onNext(result.data.map { it.toDomain() })
        else
          emitter.onError(error)
      }
    }

  private fun Category.toDomain(): GifCategory =
    GifCategory(
      name = name,
      nameEncoded = nameEncoded,
      gif = gif?.toDomain() ?: Gif.EMPTY,
      subCategories = subCategories?.map { it.toDomain() } ?: emptyList()
    )

  private fun Media.toDomain(): Gif =
    Gif(
      id = id,
      title = title,
      url = images?.fixedHeight?.gifUrl ?: images.fixedWidth.gifUrl ?: ""
    )
}