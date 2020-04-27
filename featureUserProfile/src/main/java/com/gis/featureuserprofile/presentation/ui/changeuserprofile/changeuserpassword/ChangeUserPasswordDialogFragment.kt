package com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserpassword

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.gis.repository.domain.entity.ValidationResult.*
import com.gis.utils.presentation.BaseView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featureuserprofile.R
import com.gis.featureuserprofile.databinding.DialogFragmentChangeUserPasswordBinding
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserpassword.ChangeUserPasswordIntent.Cancel
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserpassword.ChangeUserPasswordIntent.ChangePassword
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class ChangeUserPasswordDialogFragment : DialogFragment(),
  BaseView<ChangeUserPasswordState> {

  private var binding: DialogFragmentChangeUserPasswordBinding? = null
  private lateinit var intentsSubscription: Disposable
  private val viewModel: ChangeUserPasswordViewModel by viewModel()

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
      R.layout.dialog_fragment_change_user_password,
      null,
      false
    )
  }

  private fun initDialog() =
    AlertDialog.Builder(context!!)
      .setView(binding!!.root)
      .create()

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(

      RxView.clicks(binding!!.tvOk)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map {
          ChangePassword(binding!!.etOldPassword.text.toString(), binding!!.etNewPassword.text.toString())
        },

      RxView.clicks(binding!!.tvCancel)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { Cancel }
    ))
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: ChangeUserPasswordState) {
    binding!!.loading = state.loading

    if (state.closeDialog) {
      dismiss()
    }
    handleErrors(state)
  }

  private fun handleErrors(state: ChangeUserPasswordState) {
    when (state.passwordValidationResult.oldPasswordValidation) {
      is Valid -> binding!!.tilOldPassword.error = null
      is Empty -> binding!!.tilOldPassword.error = getString(R.string.error_empty)
      is TooShort -> binding!!.tilOldPassword.error = getString(R.string.error_too_short)
      is Invalid -> binding!!.tilOldPassword.error = getString(R.string.error_invalid)
    }

    when (state.passwordValidationResult.newPasswordValidation) {
      is Valid -> binding!!.tilNewPassword.error = null
      is Empty -> binding!!.tilNewPassword.error = getString(R.string.error_empty)
      is TooShort -> binding!!.tilNewPassword.error = getString(R.string.error_too_short)
      is Invalid -> binding!!.tilNewPassword.error = getString(R.string.error_invalid)
    }

    state.error?.let {
      Snackbar.make(binding!!.root, it.message!!, Snackbar.LENGTH_SHORT).show()
    }
  }
}