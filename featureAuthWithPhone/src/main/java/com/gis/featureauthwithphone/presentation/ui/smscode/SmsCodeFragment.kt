package com.gis.featureauthwithphone.presentation.ui.smscode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gis.utils.presentation.BaseView
import com.gis.utils.presentation.ui.SmsCodeInputEvents.SmsCodeInputFinished
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featureauthwithphone.R
import com.gis.featureauthwithphone.databinding.FragmentSmsCodeBinding
import com.gis.featureauthwithphone.presentation.ui.smscode.SmsCodeIntent.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SmsCodeFragment : Fragment(), BaseView<SmsCodeState> {

  private val phone: String by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getString("phone", "") }
  private var binding: FragmentSmsCodeBinding? = null
  private lateinit var intentsSubscription: Disposable
  private val viewModel: SmsCodeViewModel by viewModel()

  companion object {
    fun getInstance(phone: String): SmsCodeFragment =
      SmsCodeFragment().apply {
        arguments = Bundle().apply {
          putString("phone", phone)
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

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sms_code, container, false)
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(

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
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

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