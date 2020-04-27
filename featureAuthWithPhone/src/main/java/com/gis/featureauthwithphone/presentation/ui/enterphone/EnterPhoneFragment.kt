package com.gis.featureauthwithphone.presentation.ui.enterphone

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.gis.featureauthwithphone.R
import com.gis.featureauthwithphone.databinding.FragmentAuthWithPhoneBinding
import com.gis.featureauthwithphone.presentation.ui.enterphone.EnterPhoneIntent.*
import com.gis.repository.domain.entity.ValidationResult.Invalid
import com.gis.repository.domain.entity.ValidationResult.Valid
import com.gis.utils.domain.ImageLoader
import com.gis.utils.hideKeyboard
import com.gis.utils.presentation.BaseMviFragment
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class EnterPhoneFragment : BaseMviFragment<EnterPhoneState, FragmentAuthWithPhoneBinding, EnterPhoneViewModel>() {

  override val layoutId: Int = R.layout.fragment_auth_with_phone
  override val viewModel: EnterPhoneViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()

  override fun userIntents(): Observable<Any> =
    Observable.merge(listOf(

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