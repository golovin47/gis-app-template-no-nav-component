package com.gis.featureuserprofile.presentation.ui.extrapermissionsdialog

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.gis.utils.presentation.BaseView
import com.gis.featureuserprofile.R
import com.gis.featureuserprofile.presentation.ui.extrapermissionsdialog.ExtraPermissionsIntent.Cancel
import com.gis.featureuserprofile.presentation.ui.extrapermissionsdialog.ExtraPermissionsIntent.OpenSettings
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ExtraPermissionsDialogFragment : DialogFragment(), BaseView<ExtraPermissionsState> {

  private val EXTRA_DIALOG_PERMISSIONS_REQUEST = 0x095

  private val intentsPublisher = PublishSubject.create<ExtraPermissionsIntent>()
  private lateinit var intentsSubscription: Disposable
  private val viewModel: ExtraPermissionsViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    initIntents()
    isCancelable = false
    return initDialog()
  }

  private fun initDialog() =
    AlertDialog.Builder(context!!).apply {
      setTitle(R.string.permissions_dialog_title)
      setMessage(R.string.permissions_dialog_message)
      setCancelable(false)
      setPositiveButton(R.string.go_to_settings) { _: DialogInterface, _: Int ->
        intentsPublisher.onNext(OpenSettings)
      }
      setNegativeButton(R.string.cancel) { _: DialogInterface, _: Int ->
        intentsPublisher.onNext(Cancel)
      }
    }.create()

  override fun onDestroyView() {
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(
      listOf(
        intentsPublisher
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
      )
    )
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  private fun goToAppSettings() {
    val intent = Intent(
      Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
      Uri.parse("package:" + context!!.packageName)
    )
    startActivityForResult(intent, EXTRA_DIALOG_PERMISSIONS_REQUEST)
  }

  override fun render(state: ExtraPermissionsState) {
    if (state.goToSettings) goToAppSettings()
    if (state.closeDialog) dismiss()
  }
}