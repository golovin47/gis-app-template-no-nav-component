package com.gis.featurechat.presentation.ui.galleryimages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gis.featurechat.R
import com.gis.featurechat.databinding.FragmentGalleryImagesBinding
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImageListItem.GalleryImageItem
import com.gis.featurechat.presentation.ui.galleryimages.GalleryImagesIntents.*
import com.gis.repository.domain.entity.ChatMessage.ChatPhotoMessage
import com.gis.utils.domain.ImageLoader
import com.gis.utils.presentation.BaseMviFragment
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class GalleryImagesFragment :
  BaseMviFragment<GalleryImagesState, FragmentGalleryImagesBinding, GalleryImagesViewModel>() {

  override val layoutId: Int = R.layout.fragment_gallery_images
  private val intentsPublisher = PublishSubject.create<GalleryImagesIntents>()
  override val viewModel: GalleryImagesViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()
  private lateinit var currentState: GalleryImagesState

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    initRecyclerView()
    return binding!!.root
  }

  private fun initRecyclerView() {
    with(binding!!.rvGalleryImages) {
      adapter = GalleryImagesAdapter(intentsPublisher, imageLoader)
      layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
      addItemDecoration(GalleryImagesItemDecoration(context))
    }
  }

  override fun userIntents(): Observable<Any> =
    Observable.merge(
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