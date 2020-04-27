package com.gis.featureuserprofile.presentation.ui.imagesourcedialog

import android.net.Uri
import com.gis.utils.presentation.BaseViewModel
import com.gis.featureuserprofile.presentation.ui.imagesourcedialog.ImageSourceIntent.*
import com.gis.featureuserprofile.presentation.ui.imagesourcedialog.ImageSourceStateChange.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class ImageSourceViewModel(
  private var createFileAndGetUri: (() -> Observable<Uri>)?,
  private var showExtraPermissionsDialog: (() -> Unit)?,
  private var goToImagePreview: ((Uri) -> Unit)?
) : BaseViewModel<ImageSourceState>() {

  override fun initState(): ImageSourceState = ImageSourceState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(listOf(

      intentStream.ofType(ShowExtraPermissionsDialog::class.java)
        .doOnNext { showExtraPermissionsDialog?.invoke()},

      intentStream.ofType(TakePhoto::class.java)
        .switchMap { event ->
          createFileAndGetUri!!.invoke()
            .flatMap { uri -> Observable.just(OpenCamera(uri), Idle) }
            .cast(ImageSourceStateChange::class.java)
            .onErrorResumeNext { e: Throwable -> Observable.just(Error(e), HideError) }
            .subscribeOn(Schedulers.io())
        },

      intentStream.ofType(ChooseFromGallery::class.java)
        .flatMap { uri -> Observable.just(OpenGallery, Idle) },

      intentStream.ofType(ImageChosen::class.java)
        .doOnNext { event -> goToImagePreview?.invoke(event.uri) }
        .map { CloseDialog },

      intentStream.ofType(Cancel::class.java)
        .map { CloseDialog }
    ))

  override fun reduceState(previousState: ImageSourceState, stateChange: Any): ImageSourceState =
    when (stateChange) {
      is Idle -> previousState.copy(openCamera = false, openGallery = false, closeDialog = false, error = null)

      is OpenCamera -> previousState.copy(openCamera = true, imageUri = stateChange.uri)

      is OpenGallery -> previousState.copy(openGallery = true)

      is CloseDialog -> previousState.copy(closeDialog = true)

      is Error -> previousState.copy(error = stateChange.error)

      is HideError -> previousState.copy(error = null)

      else -> previousState
    }

  override fun onCleared() {
    createFileAndGetUri = null
    showExtraPermissionsDialog = null
    goToImagePreview = null
    super.onCleared()
  }
}