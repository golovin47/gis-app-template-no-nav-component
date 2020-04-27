package com.gis.featureuserprofile.presentation.ui.userprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gis.repository.domain.entity.User
import com.gis.utils.presentation.BaseView
import com.gis.utils.domain.ImageLoader
import com.gis.utils.toSimpleDate
import com.gis.utils.toDayMonthYearString
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.gis.featureuserprofile.R
import com.gis.featureuserprofile.databinding.FragmentUserProfileBinding
import com.gis.featureuserprofile.presentation.ui.userprofile.UserProfileIntent.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.concurrent.TimeUnit

class UserProfileFragment : Fragment(), BaseView<UserProfileState> {

  private var binding: FragmentUserProfileBinding? = null
  private var currentState: UserProfileState? = null
  private var ctblIntentsPublisher = PublishSubject.create<UserProfileIntent>()
  private lateinit var intentsSubscription: Disposable
  private val viewModel: UserProfileViewModel by viewModel()
  private val imageLoader: ImageLoader by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initCollapsingTbLayout()
    initIntents()
    return binding!!.root
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(
      inflater,
      R.layout.fragment_user_profile,
      container,
      false
    )
  }

  private fun initCollapsingTbLayout() {
    binding!!.ctblProfile.setCollapsedTitleTextColor(ContextCompat.getColor(context!!, android.R.color.white))
    binding!!.ablProfile.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
      if (Math.abs(i) - appBarLayout.totalScrollRange == 0) {
        if (binding!!.ctblProfile.title == null)
          binding!!.ctblProfile.title = getString(R.string.profile)
        if (currentState != null && currentState!!.ctblExpanded)
          ctblIntentsPublisher.onNext(CollapseCtbl)
      } else {
        if (binding!!.ctblProfile.title != null)
          binding!!.ctblProfile.title = null
        if (i == 0 && currentState != null && !currentState!!.ctblExpanded)
          ctblIntentsPublisher.onNext(ExpandCtbl)
      }
    })
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(

      Observable.just(StartObserveUser),

      ctblIntentsPublisher,

      RxView.clicks(binding!!.tvUsername)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { ChangeUsername },

      RxView.clicks(binding!!.tvEmail)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { ChangeEmail },

      RxView.clicks(binding!!.tvPhone)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { ChangePhone },

      RxView.clicks(binding!!.tvBirthdate)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map {
          val calendar = GregorianCalendar().apply { time = binding!!.tvBirthdate.text.toString().toSimpleDate() }
          val year = calendar.get(Calendar.YEAR)
          val month = calendar.get(Calendar.MONTH)
          val day = calendar.get(Calendar.DAY_OF_MONTH)
          ChangeBirthDate(year, month, day)
        },

      RxView.clicks(binding!!.tvChangePassword)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { ChangePassword },

      RxView.clicks(binding!!.ctblProfile)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { ChangeAvatarUrl },

      RxCompoundButton.checkedChanges(binding!!.switcherUseFingerprint)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .filter { enable -> currentState != null && currentState!!.user.fingerprintEnabled != enable }
        .map { enable -> EnableFingerprint(enable) },

      RxView.clicks(binding!!.btnSignOut)
        .throttleFirst(1000, TimeUnit.MILLISECONDS)
        .map { SignOut }
    ))
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  private fun setUserInfo(state: UserProfileState) {
    if (!state.user.avatarUrl.isEmpty())
      imageLoader.loadImg(binding!!.ivAvatar, state.user.avatarUrl, false)
    binding!!.tvUsername.text = state.user.username
    binding!!.tvEmail.text = state.user.email
    binding!!.tvPhone.text = state.user.phone
    binding!!.tvBirthdate.text = state.user.birthDate.toDayMonthYearString()
    if (binding!!.switcherUseFingerprint.isChecked != state.user.fingerprintEnabled && state.user != User.EMPTY)
      binding!!.switcherUseFingerprint.isChecked = state.user.fingerprintEnabled
  }

  override fun render(state: UserProfileState) {
    state.error?.run {
      Snackbar.make(binding!!.root, message!!, Snackbar.LENGTH_SHORT).show()
    }
    setUserInfo(state)
    binding!!.ablProfile.setExpanded(state.ctblExpanded, false)
    currentState = state
  }
}
