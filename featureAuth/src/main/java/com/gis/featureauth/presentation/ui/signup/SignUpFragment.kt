package com.gis.featureauth.presentation.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gis.repository.domain.entity.ValidationResult.*
import com.gis.utils.presentation.BaseView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featureauth.R
import com.gis.featureauth.databinding.FragmentSignUpBinding
import com.gis.featureauth.presentation.ui.signup.SignUpIntent.*
import com.gis.featureauth.presentation.ui.smscode.SmsCodeDialogFragment
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SignUpFragment : Fragment(), BaseView<SignUpState> {

  private var binding: FragmentSignUpBinding? = null
  private lateinit var intentsSubscription: Disposable
  private val viewModel: SignUpViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initIntents()

    return binding!!.root
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(

      RxView.clicks(binding!!.btnSignUp)
        .map {
          if (binding!!.etPhone.text.toString().isNotBlank() ||
            (binding!!.etPhone.hasFocus() &&
                binding!!.etEmail.text!!.isBlank() &&
                binding!!.etUsername.text!!.isBlank() &&
                binding!!.etPassword.text!!.isBlank())
          )
            SignUpWithPhone(phone = binding!!.etPhone.text.toString())
          else
            SignUpWithCreds(
              email = binding!!.etEmail.text.toString(),
              username = binding!!.etUsername.text.toString(),
              password = binding!!.etPassword.text.toString()
            )
        },

      RxView.clicks(binding!!.ivFacebook)
        .map { SignUpWithFacebook },

      RxView.clicks(binding!!.ivTwitter)
        .map { SignUpWithTwitter },

      RxView.clicks(binding!!.ivInstagram)
        .map { SignUpWithInstagram },

      RxView.clicks(binding!!.ivGoogle)
        .map { SignUpWithGoogle },

      RxView.clicks(binding!!.tvSignIn)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .map { GoToSignInScreen }
    ))
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: SignUpState) {
    binding!!.loading = state.loading

    if (state.showSmsCodeDialog){
      SmsCodeDialogFragment.getInstance(binding!!.etPhone.text.toString()).show(fragmentManager!!, "SmsCodeDialogFragment")
    }

    handleErrors(binding!!, state)
  }

  private fun handleErrors(
    fragmentSignUpBinding: FragmentSignUpBinding,
    signUpState: SignUpState
  ) {
    when (signUpState.signUpCredsValidationResult.emailValidation) {
      Empty -> fragmentSignUpBinding.tilEmail.error = getString(R.string.error_empty)
      Invalid -> fragmentSignUpBinding.tilEmail.error = getString(R.string.error_invalid)
      Valid -> fragmentSignUpBinding.tilEmail.error = null
    }

    when (signUpState.signUpCredsValidationResult.usernameValidation) {
      Empty -> fragmentSignUpBinding.tilUsername.error = getString(R.string.error_empty)
      TooShort -> fragmentSignUpBinding.tilUsername.error = getString(R.string.error_too_short)
      Valid -> fragmentSignUpBinding.tilUsername.error = null
    }

    when (signUpState.signUpCredsValidationResult.passwordValidation) {
      Empty -> fragmentSignUpBinding.tilPassword.error = getString(R.string.error_empty)
      TooShort -> fragmentSignUpBinding.tilPassword.error = getString(R.string.error_too_short)
      Valid -> fragmentSignUpBinding.tilPassword.error = null
    }

    when (signUpState.phoneValidationResult) {
      Empty -> fragmentSignUpBinding.tilPhone.error = getString(R.string.error_empty)
      Invalid -> fragmentSignUpBinding.tilPhone.error = getString(R.string.error_invalid)
      Valid -> fragmentSignUpBinding.tilPhone.error = null
    }

    signUpState.error?.run {
      Snackbar.make(fragmentSignUpBinding.root, message!!, LENGTH_SHORT).show()
    }
  }
}