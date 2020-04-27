package com.gis.featureuserprofile.presentation.ui.userimagepreview

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gis.featureuserprofile.R
import com.gis.featureuserprofile.presentation.ui.userimagepreview.UserImagePreviewIntent.Cancel
import com.gis.featureuserprofile.presentation.ui.userimagepreview.UserImagePreviewIntent.UpdateAvatar
import com.gis.utils.databinding.FragmentImagePreviewBinding
import com.gis.utils.domain.ImageLoader
import com.gis.utils.presentation.BaseMviFragment
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class UserImagePreviewFragment :
  BaseMviFragment<UserImagePreviewState, FragmentImagePreviewBinding, UserImagePreviewViewModel>() {

  override val layoutId: Int = R.layout.fragment_image_preview
  private val imageUri: Uri by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getParcelable<Uri>("imageUri")!! }
  override val viewModel: UserImagePreviewViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()
  private var currentStateUser: UserImagePreviewState? = null

  companion object {
    fun getInstance(imageUri: Uri) =
      UserImagePreviewFragment().apply {
        arguments = Bundle().apply {
          putParcelable("imageUri", imageUri)
        }
      }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    imageLoader.loadImg(binding!!.ivPreview, imageUri, false)
    return binding!!.root
  }

  override fun userIntents(): Observable<Any> =
    Observable.merge(listOf(
      RxView.clicks(binding!!.ivAccept)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map {
          UpdateAvatar(currentStateUser!!.user!!.copy(avatarUrl = imageUri.toString()))
        },

      RxView.clicks(binding!!.ivCancel)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { Cancel }
    ))

  override fun render(stateUser: UserImagePreviewState) {
    currentStateUser = stateUser

    stateUser.error?.let {
      Snackbar.make(binding!!.root, it.message!!, Snackbar.LENGTH_SHORT).show()
    }
  }
}