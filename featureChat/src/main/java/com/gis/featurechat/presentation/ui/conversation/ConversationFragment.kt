package com.gis.featurechat.presentation.ui.conversation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.gis.repository.domain.entity.ChatMessage.ChatFileMessage
import com.gis.repository.domain.entity.ChatMessage.ChatTextMessage
import com.gis.utils.presentation.BaseView
import com.gis.utils.domain.ImageLoader
import com.gis.utils.getFileNameAndSize
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.gis.featurechat.R
import com.gis.featurechat.databinding.FragmentConversationBinding
import com.gis.featurechat.presentation.ui.conversation.ConversationIntent.*
import com.gis.featurechat.presentation.ui.conversation.GifListItem.GifLoadingItem
import com.gis.featurechat.presentation.ui.conversation.GifListItem.GifSearchEmptyItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

private const val CAMERA_PERMISSIONS_REQUEST = 0x099
private const val GALLERY_PERMISSIONS_REQUEST = 0x098
private const val CAMERA_PHOTO_REQUEST = 0x097
private const val PICK_FILE_REQUEST = 0x096

class ConversationFragment : Fragment(), BaseView<ConversationState> {

  private lateinit var currentState: ConversationState
  private var binding: FragmentConversationBinding? = null
  private var intentsPublisher = PublishSubject.create<ConversationIntent>()
  private lateinit var intentsSubscription: Disposable
  private val viewModel: ConversationViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()

  companion object {
    fun getInstance(chatId: Int) =
      ConversationFragment().apply {
        arguments = Bundle().apply {
          putInt("chatId", chatId)
        }
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initMessagesList()
    initGifsList()
    initIntents()
    return binding!!.root
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
      CAMERA_PHOTO_REQUEST -> {
        if (resultCode == RESULT_OK) intentsPublisher.onNext(ImageChosen(currentState.imageUri!!))
      }

      PICK_FILE_REQUEST -> {
        if (resultCode == RESULT_OK) {
          val uri = data?.data!!
          val nameAndSize = context!!.getFileNameAndSize(uri)
          intentsPublisher.onNext(
            SendFileMessage(
              ChatFileMessage(
                id = System.currentTimeMillis().toInt(),
                fileName = nameAndSize.first,
                fileSize = nameAndSize.second,
                author = currentState.author,
                date = Date(),
                loading = false,
                success = false,
                error = false
              )
            )
          )
        }
      }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == GALLERY_PERMISSIONS_REQUEST)
      if (grantResults.isNotEmpty())
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
          intentsPublisher.onNext(ChooseFromGallery)
        else
          intentsPublisher.onNext(ShowExtraPermissionsDialog)

    if (requestCode == CAMERA_PERMISSIONS_REQUEST)
      if (grantResults.isNotEmpty())
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
          intentsPublisher.onNext(TakePhoto)
        else
          intentsPublisher.onNext(ShowExtraPermissionsDialog)
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversation, container, false)
  }

  private fun initMessagesList() {
    with(binding!!.rvMessages) {
      addItemDecoration(MessagesItemDecoration(context!!))
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
      adapter = MessagesAdapter(imageLoader)
    }
  }

  private fun initGifsList() {
    with(binding!!.rvGifs) {
      addItemDecoration(GifsItemDecoration(context!!))
      layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
      adapter = GifsAdapter(imageLoader = imageLoader, intentsPublisher = intentsPublisher)
      addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          loadNextGifsPageIfNeed(recyclerView, dx)
        }
      })
    }
  }

  private fun loadNextGifsPageIfNeed(rv: RecyclerView, dx: Int) {
    val llm = rv.layoutManager as LinearLayoutManager
    val totalItemCount = llm.itemCount
    val lastVisibleItem = llm.findLastVisibleItemPosition()
    if (!currentState.gifs.contains(GifLoadingItem) &&
      !currentState.gifsSearch.contains(GifSearchEmptyItem) &&
      totalItemCount <= lastVisibleItem + 4 &&
      dx > 0
    ) {
      val offset =
        if (currentState.gifsSearch.isEmpty()) currentState.gifs.size + 1 else currentState.gifsSearch.size + 1
      intentsPublisher.onNext(
        if (currentState.gifsSearch.isEmpty()) GetNextGifsPage(offset)
        else GetNextSearchGifsPage(binding!!.etTextMessage.text.toString(), offset)
      )
    }
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(
      listOf(

        intentsPublisher,

        Observable.just(ObserveConversation(arguments!!.getInt("chatId"))),

        RxView.clicks(binding!!.ivGallery)
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
          .doOnNext { if (!galleryPermissionsGranted()) requestGalleryPermissions() }
          .filter { galleryPermissionsGranted() }
          .map { ChooseFromGallery },

        RxView.clicks(binding!!.ivCamera)
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
          .doOnNext { if (!cameraPermissionsGranted()) requestCameraPermissions() }
          .filter { cameraPermissionsGranted() }
          .map { TakePhoto },

        RxView.clicks(binding!!.ivGif)
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
          .map { if (currentState.gifsOpened) CloseGifs else OpenGifs(currentState.gifs.isEmpty()) },

        RxView.clicks(binding!!.ivFile)
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
          .doOnNext { chooseFile() },

        RxTextView.textChanges(binding!!.etTextMessage)
          .filter { binding!!.etTextMessage.hint == getString(R.string.search_gif_hint) }
          .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
          .map { SearchGifs(binding!!.etTextMessage.text.toString(), 0) },

        RxView.clicks(binding!!.ivSend)
          .filter { !binding!!.etTextMessage.text.isBlank() }
          .throttleFirst(500, TimeUnit.MILLISECONDS)
          .map {
            SendTextMessage(
              ChatTextMessage(
                id = System.currentTimeMillis().toInt(),
                text = binding!!.etTextMessage.text.toString().trim(),
                author = currentState.author,
                date = Date(),
                loading = false,
                success = false,
                error = false
              )
            )
          }
          .doOnNext { binding!!.etTextMessage.text.clear() }
      )
    )
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  private fun cameraPermissionsGranted(): Boolean =
    (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED)

  private fun requestCameraPermissions() {
    requestPermissions(
      arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
      CAMERA_PERMISSIONS_REQUEST
    )
  }

  private fun openCamera(uri: Uri) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
      putExtra(MediaStore.EXTRA_OUTPUT, uri)
    }
    startActivityForResult(intent, CAMERA_PHOTO_REQUEST)
  }

  private fun galleryPermissionsGranted(): Boolean =
    (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED)

  private fun requestGalleryPermissions() {
    requestPermissions(
      arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
      GALLERY_PERMISSIONS_REQUEST
    )
  }

  private fun chooseFile() {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
      addCategory(Intent.CATEGORY_OPENABLE)
      type = "*/*"
    }
    startActivityForResult(intent, PICK_FILE_REQUEST)
  }

  override fun render(state: ConversationState) {

    if (state.openCamera) openCamera(state.imageUri!!)

    binding!!.tvConversationName.text = state.name

    if (!state.avatarUrl.isEmpty()) {
      imageLoader.loadImg(binding!!.ivConversationAvatar, state.avatarUrl, true)
    }

    val onBottom =
      (binding!!.rvMessages.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0
    if (state.messageSent ||
      (this::currentState.isInitialized && currentState.messages.size < state.messages.size && onBottom)
    ) {
      binding?.rvMessages?.adapter = MessagesAdapter(imageLoader)
    }
    (binding!!.rvMessages.adapter as MessagesAdapter).submitList(state.messages)

    if (this::currentState.isInitialized && currentState.gifsOpened != state.gifsOpened)
      TransitionManager.beginDelayedTransition(binding!!.rootView)
    if (state.gifsOpened) {
      binding!!.gifTopGuideline.setGuidelinePercent(0.8f)
      binding!!.gifBottomGuideline.setGuidelinePercent(1f)
    } else {
      binding!!.gifTopGuideline.setGuidelinePercent(1f)
      binding!!.gifBottomGuideline.setGuidelinePercent(1.2f)
    }

    with(binding!!.rvGifs.adapter as GifsAdapter){
      submitList(if (state.gifsSearch.isEmpty()) state.gifs else state.gifsSearch)
      user = state.author
    }

    binding!!.etTextMessage.setHint(if (state.gifsOpened) R.string.search_gif_hint else R.string.type_your_message)

    state.error?.run {
      Snackbar.make(binding!!.root, message!!, Snackbar.LENGTH_SHORT).show()
    }

    currentState = state
  }
}