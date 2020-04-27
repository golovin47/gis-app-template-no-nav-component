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
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SignInFragment : Fragment(), BaseView<SignInState> {

  private var binding: FragmentSignInBinding? = null
  private val intentsPublisher = PublishSubject.create<SignInIntent>()
  private lateinit var intentsSubscription: Disposable
  private val viewModel: SignInViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initFingerprint()
    initIntents()
    return binding!!.root
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
  }

  private fun initFingerprint() {
    binding!!.ivFingerPrint.visibility = if (isFingerprintAvailable(context!!)) VISIBLE else GONE
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(

      intentsPublisher,

      RxView.clicks(binding!!.btnSignIn)
        .map {
          if (binding!!.etPhone.text.toString().isNotBlank() ||
            (binding!!.etPhone.hasFocus() &&
                binding!!.etEmail.text!!.isBlank() &&
                binding!!.etPassword.text!!.isBlank())
          )
            SignInWithPhone(phone = binding!!.etPhone.text.toString())
          else
            SignInWithCreds(
              email = binding!!.etEmail.text.toString(),
              password = binding!!.etPassword.text.toString()
            )
        },

      RxView.clicks(binding!!.ivFacebook)
        .map { SignInWithFacebook },

      RxView.clicks(binding!!.ivTwitter)
        .map { SignInWithTwitter },

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

    )).subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

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