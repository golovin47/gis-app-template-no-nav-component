package com.gis.featureauthwithphone.presentation.ui.smscode

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.gis.featureauthwithphone.R
import com.gis.featureauthwithphone.databinding.FragmentSmsCodeBinding
import com.gis.featureauthwithphone.presentation.ui.smscode.SmsCodeIntent.*
import com.gis.utils.presentation.BaseMviFragment
import com.gis.utils.presentation.ui.SmsCodeInputEvents.SmsCodeInputFinished
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SmsCodeFragment : BaseMviFragment<SmsCodeState, FragmentSmsCodeBinding, SmsCodeViewModel>() {

  override val layoutId: Int = R.layout.fragment_sms_code
  private val phone: String by lazy(LazyThreadSafetyMode.NONE) {
    arguments!!.getString(
      "phone",
      ""
    )
  }
  override val viewModel: SmsCodeViewModel by viewModel()

  companion object {
    fun getInstance(phone: String): SmsCodeFragment =
      SmsCodeFragment().apply {
        arguments = Bundle().apply {
          putString("phone", phone)
        }
      }
  }

  override fun userIntents(): Observable<Any> =
    Observable.merge(listOf(

      binding!!.smsCodeView.observeSmsCode()
        .map {
          if (it is SmsCodeInputFinished)
            SendCode(phone, it.code)
          else
            HideSmsCodeError
        },

      RxView.clicks(binding!!.btnResend)
        .map { ResendCode },

      RxToolbar.navigationClicks(binding!!.tbSmsCode)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .map { GoBack }
    ))

  override fun render(state: SmsCodeState) {

    if (state.loading) {
      binding!!.smsCodeView.disableInput()
      binding!!.pbLoading.visibility = VISIBLE
      binding!!.btnResend.isEnabled = false
    } else {
      binding!!.smsCodeView.enableInput()
      binding!!.pbLoading.visibility = GONE
      if (state.counter < 0) {
        binding!!.btnResend.isEnabled = true
      }
    }

    if (state.counter > 0) {
      binding!!.tvCounter.text = getString(R.string.resend_code_in, state.counter.toString())
      binding!!.tvCounter.visibility = VISIBLE
      binding!!.btnResend.isEnabled = false
    } else {
      binding!!.tvCounter.visibility = GONE
      binding!!.btnResend.isEnabled = true
    }

    if (state.error != null) {
      binding!!.smsCodeView.showError()
      binding!!.tvError.text = state.error.message
      binding!!.tvError.visibility = VISIBLE
    } else {
      binding!!.smsCodeView.hideError()
      binding!!.tvError.text = ""
      binding!!.tvError.visibility = GONE
    }
  }
}