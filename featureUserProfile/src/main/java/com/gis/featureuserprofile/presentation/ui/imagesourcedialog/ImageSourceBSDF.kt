package com.gis.featureuserprofile.presentation.ui.imagesourcedialog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.gis.utils.presentation.BaseView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featureuserprofile.R
import com.gis.featureuserprofile.databinding.BsdfImageSourceBinding
import com.gis.featureuserprofile.presentation.ui.imagesourcedialog.ImageSourceIntent.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ImageSourceBSDF : BottomSheetDialogFragment(), BaseView<ImageSourceState> {

  private val CAMERA_PERMISSIONS_REQUEST = 0x097
  private val GALLERY_PERMISSIONS_REQUEST = 0x096
  private val CAMERA_PHOTO_REQUEST = 0x099
  private val GALLERY_PHOTO_REQUEST = 0x098

  private var currentState: ImageSourceState? = null
  private var intentsPublisher = PublishSubject.create<ImageSourceIntent>()
  private var binding: BsdfImageSourceBinding? = null
  private lateinit var intentsSubscription: Disposable
  private val viewModel: ImageSourceViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initShowListener()
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
      CAMERA_PHOTO_REQUEST ->
        if (resultCode == Activity.RESULT_OK)
          intentsPublisher.onNext(ImageChosen(currentState!!.imageUri!!))

      GALLERY_PHOTO_REQUEST ->
        if (resultCode == Activity.RESULT_OK)
          intentsPublisher.onNext(ImageChosen(data!!.data!!))
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    if (requestCode == CAMERA_PERMISSIONS_REQUEST || requestCode == GALLERY_PERMISSIONS_REQUEST)
      if (grantResults.size == 2)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
          grantResults[1] == PackageManager.PERMISSION_GRANTED
        )
          intentsPublisher.onNext(if (requestCode == CAMERA_PERMISSIONS_REQUEST) TakePhoto else ChooseFromGallery)
        else
          intentsPublisher.onNext(ShowExtraPermissionsDialog)
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.bsdf_image_source, container, false)
  }

  private fun initShowListener() {
    dialog?.setOnShowListener {
      val bsd = it as BottomSheetDialog
      val bsdView = bsd.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
      BottomSheetBehavior.from(bsdView).state = BottomSheetBehavior.STATE_EXPANDED
    }
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(

      intentsPublisher,

      RxView.clicks(binding!!.tvTakePhoto)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .doOnNext { if (!permissionsGranted()) requestPermissions(CAMERA_PERMISSIONS_REQUEST) }
        .filter { permissionsGranted() }
        .map { TakePhoto },

      RxView.clicks(binding!!.tvToGallery)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { ChooseFromGallery },

      RxView.clicks(binding!!.tvCancel)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { Cancel }
    ))
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  private fun permissionsGranted(): Boolean {
    return (ContextCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED) &&
        (ContextCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED)
  }

  private fun requestPermissions(requestCode: Int) {
    requestPermissions(
      arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
      requestCode
    )
  }

  private fun openCamera(uri: Uri) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
      putExtra(MediaStore.EXTRA_OUTPUT, uri)
    }
    startActivityForResult(intent, CAMERA_PHOTO_REQUEST)
  }

  private fun openGallery() {
    startActivityForResult(Intent(Intent.ACTION_PICK).apply { type = "image/*" }, GALLERY_PHOTO_REQUEST)
  }


  override fun render(state: ImageSourceState) {
    currentState = state

    with(state) {
      if (closeDialog) dismiss()

      if (openCamera) openCamera(imageUri!!)

      if (openGallery) openGallery()

      error?.let {
        Snackbar.make(binding!!.root, it.message!!, Snackbar.LENGTH_SHORT).show()
      }
    }
  }
}