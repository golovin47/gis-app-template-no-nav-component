package com.gis.featurechat.presentation.ui.galleryimages

import com.gis.repository.domain.interactors.GetAllGalleryImagesUseCase
import com.gis.repository.domain.interactors.ObserveUserUseCase
import com.gis.repository.domain.interactors.SendChatPhotoMessageUseCase
import com.gis.utils.presentation.BaseViewModel
import io.reactivex.Observable
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImagesStateChange.*
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImagesIntents.*
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImageListItem.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GalleryImagesViewModel(
  private var goBack: (() -> Unit)?,
  private val observeUserUseCase: ObserveUserUseCase,
  private val getAllGalleryImagesUseCase: GetAllGalleryImagesUseCase,
  private val sendChatPhotoMessageUseCase: SendChatPhotoMessageUseCase
) : BaseViewModel<GalleryImagesState>() {

  override fun initState(): GalleryImagesState = GalleryImagesState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(
      listOf(
        observeUserUseCase.execute()
          .map { UserReceived(it) }
          .cast(GalleryImagesStateChange::class.java)
          .onErrorResumeNext { e: Throwable -> handleError(e) }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread()),

        getAllGalleryImagesUseCase.execute()
          .map { GalleryImagesReceived(imagePathsToListItems(it)) }
          .cast(GalleryImagesStateChange::class.java)
          .onErrorResumeNext { e: Throwable -> handleError(e) }
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
      )
    )

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(
        intentStream.ofType(StartObserve::class.java)
          .map { ObservingStarted },

        intentStream.ofType(SelectImage::class.java)
          .map { event -> ImageSelected(event.path) },

        intentStream.ofType(UnselectImage::class.java)
          .map { event -> ImageUnselected(event.path) },

        intentStream.ofType(SendImages::class.java)
          .switchMap { event ->
            Observable.fromIterable(event.images)
              .flatMap { message ->
                sendChatPhotoMessageUseCase.execute(message)
                  .toSingleDefault(Idle)
                  .toObservable()
                  .cast(GalleryImagesStateChange::class.java)
                  .onErrorResumeNext { e: Throwable -> handleError(e) }
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
              }
              .doOnSubscribe { goBack?.invoke() }
          },

        intentStream.ofType(Cancel::class.java)
          .doOnNext { goBack?.invoke() }
      )
    )

  private fun imagePathsToListItems(paths: List<String>) =
    paths.map { path -> GalleryImageItem(path, false) }

  private fun handleError(e: Throwable) = Observable.just(Error(e), HideError)

  override fun reduceState(previousState: GalleryImagesState, stateChange: Any): GalleryImagesState =
    when (stateChange) {
      is StartLoading -> previousState.copy(loading = true, error = null)

      is UserReceived -> previousState.copy(user = stateChange.user)

      is GalleryImagesReceived -> previousState.copy(loading = false, images = stateChange.images)

      is ImageSelected -> previousState.copy(images =
      previousState.images.toMutableList().map { item ->
        if (item is GalleryImageItem && item.path == stateChange.path) item.copy(selected = true)
        else item
      }, selectedCount = previousState.images.count { item -> item is GalleryImageItem && item.selected } + 1)

      is ImageUnselected -> previousState.copy(images =
      previousState.images.toMutableList().map { item ->
        if (item is GalleryImageItem && item.path == stateChange.path) item.copy(selected = false)
        else item
      }, selectedCount = previousState.images.count { item -> item is GalleryImageItem && item.selected } - 1)

      is Error -> previousState.copy(error = stateChange.error)

      is HideError -> previousState.copy(error = null)

      else -> previousState
    }

  override fun onCleared() {
    goBack = null
    super.onCleared()
  }
}