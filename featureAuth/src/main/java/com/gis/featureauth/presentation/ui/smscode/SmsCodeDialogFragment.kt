package com.gis.featureauth.presentation.ui.smscode

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.gis.utils.presentation.BaseView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featureauth.R
import com.gis.featureauth.databinding.DialogFragmentSmsCodeBinding
import com.gis.featureauth.presentation.ui.smscode.SmsCodeIntent.Cancel
import com.gis.featureauth.presentation.ui.smscode.SmsCodeIntent.Send
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class SmsCodeDialogFragment : DialogFragment(), BaseView<SmsCodeState> {

  private var binding: DialogFragmentSmsCodeBinding? = null
  private lateinit var intentsSubscription: Disposable
  private val dialogViewModel: SmsCodeDialogViewModel by viewModel()
  private val phone: String by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getString("phone", "") }

  companion object {
    fun getInstance(phone: String): SmsCodeDialogFragment =
      SmsCodeDialogFragment().apply {
        arguments = Bundle().apply {
          putString("phone", phone)
        }
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    initBinding()
    initIntents()
    isCancelable = false
    return initDialog()
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initBinding() {
    binding = DataBindingUtil.inflate(
      LayoutInflater.from(context!!),
      R.layout.dialog_fragment_sms_code,
      null,
      false
    )
  }

  private fun initDialog(): AlertDialog =
    AlertDialog.Builder(context!!)
      .setView(binding!!.root)
      .create()


  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(

      RxView.clicks(binding!!.tvSend)
        .filter { !binding!!.etSmsCode.text.isNullOrBlank() }
        .map { Send(binding!!.etSmsCode.text.toString(), phone) },

      RxView.clicks(binding!!.tvCancel)
        .map { Cancel }
    ))
      .subscribe(dialogViewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    dialogViewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: SmsCodeState) {
    binding!!.loading = state.loading

    if (state.closeDialog) {
      dismiss()
    }

    state.error?.let {
      Snackbar.make(binding!!.root, it.message!!, Snackbar.LENGTH_LONG).show()
    }
  }
}