package com.gis.featureauth.presentation.ui.signup

import com.gis.featureauth.R
import com.gis.featureauth.databinding.FragmentSignUpBinding
import com.gis.featureauth.presentation.ui.signup.SignUpIntent.*
import com.gis.featureauth.presentation.ui.smscode.SmsCodeDialogFragment
import com.gis.repository.domain.entity.ValidationResult.*
import com.gis.utils.presentation.BaseMviFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SignUpFragment : BaseMviFragment<SignUpState, FragmentSignUpBinding, SignUpViewModel>() {

  override val layoutId: Int = R.layout.fragment_sign_up
  override val viewModel: SignUpViewModel by viewModel()

  override fun userIntents(): Observable<Any> =
    Observable.merge(listOf(

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

  override fun render(state: SignUpState) {
    binding!!.loading = state.loading

    if (state.showSmsCodeDialog) {
      SmsCodeDialogFragment.getInstance(binding!!.etPhone.text.toString())
        .show(fragmentManager!!, "SmsCodeDialogFragment")
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