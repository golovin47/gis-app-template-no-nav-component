package com.gis.featureauthwithphone.presentation.ui.enterphone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gis.repository.domain.entity.ValidationResult.*
import com.gis.utils.presentation.BaseView
import com.gis.utils.domain.ImageLoader
import com.gis.utils.hideKeyboard
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.gis.featureauthwithphone.R
import com.gis.featureauthwithphone.databinding.FragmentAuthWithPhoneBinding
import com.gis.featureauthwithphone.presentation.ui.enterphone.EnterPhoneIntent.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class EnterPhoneFragment : Fragment(), BaseView<EnterPhoneState> {

  private var binding: FragmentAuthWithPhoneBinding? = null
  private lateinit var intentsSubscription: Disposable
  private val viewModel: EnterPhoneViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()

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
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth_with_phone, container, false)
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(

      Observable.just(ObserveCurrentCountry),

      RxView.clicks(binding!!.bgCountry)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .doOnNext { binding!!.bgCountry.hideKeyboard() }
        .map { ChooseCountry },

      RxTextView.textChanges(binding!!.etPhone)
        .map { phone -> PhoneInputChanged(if (phone.isNotBlank()) "${binding!!.tvCode.text}$phone" else "") },

      RxTextView.textChanges(binding!!.tvCode)
        .filter { code -> code.isNotBlank() && binding!!.etPhone.text.isNotBlank() }
        .map { code -> PhoneInputChanged("$code${binding!!.etPhone.text}") },

      RxView.clicks(binding!!.btnSend)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .map { SendPhone("${binding!!.tvCode.text}${binding!!.etPhone.text}") }
    ))
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: EnterPhoneState) {
    imageLoader.loadImg(binding!!.ivFlag, state.chosenCountry.flagResId, false)
    binding!!.ivDownArrow.visibility = View.VISIBLE
    if (binding!!.tvCode.text.toString() != state.chosenCountry.code) {
      binding!!.tvCode.text = state.chosenCountry.code
    }

    if (state.phoneValidationResult is Valid && binding!!.etPhone.text.toString() != state.formattedPhone) {
      binding!!.etPhone.setText(state.formattedPhone)
    }

    binding!!.btnSend.isEnabled = state.phoneValidationResult is Valid

    binding!!.ivError.visibility = if (state.phoneValidationResult is Invalid) VISIBLE else GONE
  }
}