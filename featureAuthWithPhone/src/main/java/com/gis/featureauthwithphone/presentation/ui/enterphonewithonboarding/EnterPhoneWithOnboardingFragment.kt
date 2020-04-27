package com.gis.featureauthwithphone.presentation.ui.enterphonewithonboarding

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.gis.repository.domain.entity.ValidationResult.Invalid
import com.gis.repository.domain.entity.ValidationResult.Valid
import com.gis.utils.domain.ImageLoader
import com.gis.utils.hideKeyboard
import com.gis.utils.presentation.BaseView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.gis.featureauthwithphone.R
import com.gis.featureauthwithphone.databinding.FragmentAuthWithPhoneOnboardingBinding
import com.gis.featureauthwithphone.presentation.ui.enterphonewithonboarding.EnterPhoneWithOnboardingIntent.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import java.util.concurrent.TimeUnit

class EnterPhoneWithOnboardingFragment : Fragment(), BaseView<EnterPhoneWithOnboardingState> {

  private var binding: FragmentAuthWithPhoneOnboardingBinding? = null
  private lateinit var intentsSubscription: Disposable
  private val viewModel: EnterPhoneWithOnboardingViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()

  private lateinit var keyboardWatcher: Unregistrar
  private var navBarHeight: Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (savedInstanceState == null) {
      initOnboardingNavigator()
      startOnboarding()
    }

    handleStates()
  }

  private fun initOnboardingNavigator() {
    get<(FragmentManager) -> Unit>(
      named("setOnboardingFragmentManager")).invoke(childFragmentManager)
    get<(Int) -> Unit>(named("setOnboardingFragmentContainer")).invoke(R.id.onboardingContainer)
  }

  private fun startOnboarding() {
    get<(Boolean) -> Unit>(named("startOnboarding")).invoke(false)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    registerKeyboardWatcher()
    initNavBarHeight()
    initIntents()
    return binding!!.root
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth_with_phone_onboarding, container, false)
  }

  private fun initNavBarHeight() {
    val activityRoot = (activity!!.findViewById<ViewGroup>(android.R.id.content)).getChildAt(0)
    activityRoot.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
      override fun onGlobalLayout() {
        activityRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
        val r = Rect()
        activityRoot.getWindowVisibleDisplayFrame(r)
        val screenHeight = activityRoot.rootView.height
        navBarHeight = screenHeight - r.bottom
      }
    })
  }

  private fun registerKeyboardWatcher() {
    keyboardWatcher = KeyboardVisibilityEvent.registerEventListener(activity) { isOpen ->
      binding?.run {
        val r = Rect()
        val activityRoot = (activity!!.findViewById<ViewGroup>(android.R.id.content)).getChildAt(0)
        activityRoot.getWindowVisibleDisplayFrame(r)
        val screenHeight = activityRoot.rootView.height
        if (!isOpen) {
          navBarHeight = screenHeight - r.bottom
        }
        val heightDiff = screenHeight - (r.height() + navBarHeight)
        rootContainer.translationY = if (isOpen) -heightDiff.toFloat() else 0f
      }
    }
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    keyboardWatcher.unregister()
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

  override fun render(state: EnterPhoneWithOnboardingState) {
    imageLoader.loadImg(binding!!.ivFlag, state.chosenCountry.flagResId, false)
    binding!!.ivDownArrow.visibility = VISIBLE
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