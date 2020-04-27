package com.gis.featurechat.presentation.ui.messageimagepreview

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gis.featurechat.R
import com.gis.featurechat.presentation.ui.messageimagepreview.MessageImagePreviewIntent.Cancel
import com.gis.featurechat.presentation.ui.messageimagepreview.MessageImagePreviewIntent.SendPhotoMessage
import com.gis.repository.domain.entity.ChatMessage.ChatPhotoMessage
import com.gis.utils.databinding.FragmentImagePreviewBinding
import com.gis.utils.domain.ImageLoader
import com.gis.utils.presentation.BaseMviFragment
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class MessageImagePreviewFragment :
  BaseMviFragment<MessageImagePreviewState, FragmentImagePreviewBinding, MessageImagePreviewViewModel>() {

  override val layoutId: Int = R.layout.fragment_image_preview
  private val imageUri: Uri by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getParcelable<Uri>("imageUri")!! }
  override val viewModel: MessageImagePreviewViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()
  private lateinit var currentState: MessageImagePreviewState

  companion object {
    fun getInstance(imageUri: Uri) =
      MessageImagePreviewFragment().apply {
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

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  override fun userIntents(): Observable<Any> =
    Observable.merge(listOf(
      RxView.clicks(binding!!.ivAccept)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map {
          SendPhotoMessage(
            ChatPhotoMessage(
              id = System.currentTimeMillis().toInt(),
              imageUrl = imageUri.toString(),
              author = currentState.user,
              date = Date(),
              loading = true,
              success = false,
              error = false
            )
          )
        },

      RxView.clicks(binding!!.ivCancel)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { Cancel }
    ))

  override fun render(state: MessageImagePreviewState) {
    currentState = state

    state.error?.run {
      Snackbar.make(binding!!.root, message!!, Snackbar.LENGTH_SHORT).show()
    }
  }
}