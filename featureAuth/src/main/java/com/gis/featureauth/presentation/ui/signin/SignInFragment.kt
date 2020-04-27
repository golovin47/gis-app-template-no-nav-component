package com.gis.featureauth.presentation.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gis.repository.domain.entity.ValidationResult.*
import com.gis.utils.isFingerprintAvailable
import com.gis.utils.presentation.BaseView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import com.jakewharton.rxbinding2.view.RxView
import com.gis.featureauth.R
import com.gis.featureauth.databinding.FragmentSignInBinding
import com.gis.featureauth.presentation.ui.signin.SignInIntent.*
import com.gis.featureauth.presentation.ui.smscode.SmsCodeDialogFragment
import com.gis.utils.presentation.BaseMviFragment
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SignInFragment : BaseMviFragment<SignInState, FragmentSignInBinding, SignInViewModel>() {

  override val layoutId: Int = R.layout.fragment_sign_in
  private val intentsPublisher = PublishSubject.create<SignInIntent>()
  override val viewModel: SignInViewModel by viewModel()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    initFingerprint()
    return binding!!.root
  }

  private fun initFingerprint() {
    binding!!.ivFingerPrint.visibility = if (isFingerprintAvailable(context!!)) VISIBLE else GONE
  }

  override fun userIntents(): Observable<Any> =
    Observable.merge(listOf(

      intentsPublisher,

      RxView.clicks(binding!!.ivInstagram)
        .map { SignInWithInstagram },

      RxView.clicks(binding!!.ivGoogle)
        .map { SignInWithGoogle },

      RxView.clicks(binding!!.tvSignUp)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .map { GoToSignUpScreen },

      RxView.clicks(binding!!.ivFingerPrint)
        .throttleFirst(500, TimeUnit.MILLISECONDS)
        .map { GetUsersAvailableForFingerprintAuth(activity!!) }

    ))

  override fun render(state: SignInState) {
    binding!!.loading = state.loading

    if (state.showSmsCodeDialog) {
      SmsCodeDialogFragment.getInstance(binding!!.etPhone.text.toString())
        .show(fragmentManager!!, "SmsCodeDialogFragment")
    }

    if (state.showChooseUserDialog) {
      intentsPublisher.onNext(ShowChooseUserDialog)
    }

    if (state.authWithUser) {
      intentsPublisher.onNext(SignInWithFingerprint(state.fingerprintUser))
    }

    handleErrors(binding!!, state)
  }

  private fun handleErrors(
    fragmentSignUpBinding: FragmentSignInBinding,
    signInState: SignInState
  ) {
    when (signInState.credsValidationResult.emailValidation) {
      Empty -> fragmentSignUpBinding.tilEmail.error = getString(R.string.error_empty)
      Invalid -> fragmentSignUpBinding.tilEmail.error = getString(R.string.error_invalid)
      Valid -> fragmentSignUpBinding.tilEmail.error = null
    }

    when (signInState.credsValidationResult.passwordValidation) {
      Empty -> fragmentSignUpBinding.tilPassword.error = getString(R.string.error_empty)
      TooShort -> fragmentSignUpBinding.tilPassword.error = getString(R.string.error_too_short)
      Valid -> fragmentSignUpBinding.tilPassword.error = null
    }

    when (signInState.phoneValidationResult) {
      Empty -> fragmentSignUpBinding.tilPhone.error = getString(R.string.error_empty)
      Invalid -> fragmentSignUpBinding.tilPhone.error = getString(R.string.error_invalid)
      Valid -> fragmentSignUpBinding.tilPhone.error = null
    }

    signInState.error?.run {
      Snackbar.make(fragmentSignUpBinding.root, message!!, LENGTH_SHORT).show()
    }
  }
}