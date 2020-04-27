package com.gis.featurechat.presentation.ui.messageimagepreview

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gis.repository.domain.entity.ChatMessage.*
import com.gis.utils.presentation.BaseView
import com.gis.utils.databinding.FragmentImagePreviewBinding
import com.gis.utils.domain.ImageLoader
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featurechat.R
import com.gis.featurechat.presentation.ui.messageimagepreview.MessageImagePreviewIntent.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class MessageImagePreviewFragment : Fragment(), BaseView<MessageImagePreviewState> {

  private val imageUri: Uri by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getParcelable<Uri>("imageUri")!! }
  private lateinit var currentState: MessageImagePreviewState
  private var binding: FragmentImagePreviewBinding? = null
  private lateinit var intentsSubscription: Disposable
  private val viewModelMessage: MessageImagePreviewViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()

  companion object {
    fun getInstance(imageUri: Uri) =
      MessageImagePreviewFragment().apply {
        arguments = Bundle().apply {
          putParcelable("imageUri", imageUri)
        }
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initIntents()
    return binding!!.root
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_preview, container, false)
    imageLoader.loadImg(binding!!.ivPreview, imageUri, false)
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(
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
      .subscribe(viewModelMessage.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModelMessage.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: MessageImagePreviewState) {
    currentState = state

    state.error?.run {
      Snackbar.make(binding!!.root, message!!, Snackbar.LENGTH_SHORT).show()
    }
  }
}