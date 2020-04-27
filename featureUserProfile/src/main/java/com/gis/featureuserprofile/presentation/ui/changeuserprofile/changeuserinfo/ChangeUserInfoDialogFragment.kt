package com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.gis.repository.domain.entity.User
import com.gis.repository.domain.entity.ValidationResult.*
import com.gis.utils.presentation.BaseView
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featureuserprofile.R
import com.gis.featureuserprofile.databinding.DialogFragmentChangeUserInfoBinding
import com.gis.featureuserprofile.presentation.ui.changeuserprofile.changeuserinfo.ChangeUserInfoIntent.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel

const val TYPE_USERNAME = "username"
const val TYPE_EMAIL = "email"
const val TYPE_PHONE = "phone"

class ChangeUserInfoDialogFragment : DialogFragment(), BaseView<ChangeUserInfoState> {

  private var binding: DialogFragmentChangeUserInfoBinding? = null
  private lateinit var intentsSubscription: Disposable
  private val viewModel: ChangeUserInfoViewModel by viewModel()
  private var currentState: ChangeUserInfoState? = null

  companion object {
    fun getInstance(type: String): ChangeUserInfoDialogFragment =
      ChangeUserInfoDialogFragment().apply {
        arguments = Bundle().apply {
          putString("type", type)
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
      R.layout.dialog_fragment_change_user_info,
      null,
      false
    )

    when (arguments!!.getString("type")) {
      TYPE_USERNAME -> {
        binding!!.tvTitle.text = getString(R.string.change_username)
        binding!!.tilUserInfo.hint = getString(R.string.username)
        binding!!.etUserInfo.inputType = InputType.TYPE_CLASS_TEXT
      }
      TYPE_EMAIL -> {
        binding!!.tvTitle.text = getString(R.string.change_email)
        binding!!.tilUserInfo.hint = getString(R.string.email)
        binding!!.etUserInfo.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
      }
      TYPE_PHONE -> {
        binding!!.tvTitle.text = getString(R.string.change_phone)
        binding!!.tilUserInfo.hint = getString(R.string.phone)
        binding!!.etUserInfo.inputType = InputType.TYPE_CLASS_PHONE
      }
    }
  }

  private fun initDialog(): AlertDialog =
    AlertDialog.Builder(context!!)
      .setView(binding!!.root)
      .create()

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(

      RxView.clicks(binding!!.tvOk)
        .map {
          when (arguments!!.getString("type")) {
            TYPE_USERNAME -> ChangeUsername(binding!!.etUserInfo.text.toString(), currentState!!.user)
            TYPE_EMAIL -> ChangeEmail(binding!!.etUserInfo.text.toString(), currentState!!.user)
            TYPE_PHONE -> ChangePhone(binding!!.etUserInfo.text.toString(), currentState!!.user)
            else -> throw IllegalArgumentException("Unknown dialog type")
          }
        },

      RxView.clicks(binding!!.tvCancel)
        .map { Cancel }
    ))
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: ChangeUserInfoState) {
    if (state.closeDialog) {
      dismiss()
    }
    setUserInfo(state)
    currentState = state
    handleErrors(state)
  }

  private fun setUserInfo(state: ChangeUserInfoState) {
    if (currentState?.user == User.EMPTY && state.user != User.EMPTY) {
      when (arguments!!.getString("type")) {
        TYPE_USERNAME -> binding!!.etUserInfo.setText(state.user.username)
        TYPE_EMAIL -> binding!!.etUserInfo.setText(state.user.email)
        TYPE_PHONE -> binding!!.etUserInfo.setText(state.user.phone)
      }
    }
  }

  private fun handleErrors(state: ChangeUserInfoState) {
    if (state.userInfoValidationError != Valid) {
      binding!!.tilUserInfo.error = when (state.userInfoValidationError) {
        Invalid -> getString(R.string.error_invalid)
        TooShort -> getString(R.string.error_too_short)
        Empty -> getString(R.string.error_empty)
        else -> ""
      }
    }

    state.error?.let {
      Snackbar.make(binding!!.root, it.message!!, Snackbar.LENGTH_SHORT).show()
    }
  }
}