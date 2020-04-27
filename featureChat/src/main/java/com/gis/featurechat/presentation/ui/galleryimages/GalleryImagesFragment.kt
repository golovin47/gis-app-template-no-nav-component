package com.gis.featurechat.presentation.ui.galleryimages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gis.repository.domain.entity.ChatMessage.*
import com.gis.repository.domain.entity.User
import com.gis.utils.presentation.BaseView
import com.gis.utils.domain.ImageLoader
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featurechat.R
import com.gis.featurechat.databinding.FragmentGalleryImagesBinding
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImagesIntents.StartObserve
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImagesIntents.*
import java.util.*
import java.util.concurrent.TimeUnit
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImageListItem.*

class GalleryImagesFragment : Fragment(), BaseView<GalleryImagesState> {

  private lateinit var currentState: GalleryImagesState
  private var binding: FragmentGalleryImagesBinding? = null
  private val intentsPublisher = PublishSubject.create<GalleryImagesIntents>()
  private lateinit var intentsSubscription: Disposable
  private val viewModel: GalleryImagesViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initRecyclerView()
    initIntents()
    return binding!!.root
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery_images, container, false)
  }

  private fun initRecyclerView() {
    with(binding!!.rvGalleryImages) {
      adapter = GalleryImagesAdapter(intentsPublisher, imageLoader)
      layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
      addItemDecoration(GalleryImagesItemDecoration(context))
    }
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(
      listOf(
        Observable.just(StartObserve),

        intentsPublisher,

        RxView.clicks(binding!!.ivSend)
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
          .map {
            SendImages(
              listItemToChatPhotoMessage(
                currentState.images.filter { item -> item is GalleryImageItem && item.selected })
            )
          },

        RxView.clicks(binding!!.tvCancel)
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
          .map { Cancel }
      )
    )
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  private fun listItemToChatPhotoMessage(listItems: List<GalleryImageListItem>) =
    listItems
      .map { item ->
        ChatPhotoMessage(
          id = System.currentTimeMillis().toInt(),
          imageUrl = (item as GalleryImageItem).path,
          author = currentState.user,
          date = Date(),
          loading = true,
          success = false,
          error = false
        )
      }

  override fun render(state: GalleryImagesState) {
    currentState = state

    (binding!!.rvGalleryImages.adapter as GalleryImagesAdapter).submitList(state.images)

    if (state.selectedCount != 0) {
      binding!!.tvCounter.text = state.selectedCount.toString()
      binding!!.tvCounter.visibility = View.VISIBLE
    } else {
      binding!!.tvCounter.visibility = View.GONE
    }

    state.error?.run {
      Snackbar.make(binding!!.root, message!!, Snackbar.LENGTH_SHORT).show()
    }
  }
}