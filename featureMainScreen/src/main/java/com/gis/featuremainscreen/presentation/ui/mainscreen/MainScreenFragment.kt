package com.gis.featuremainscreen.presentation.ui.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gis.utils.presentation.BackPressHandler
import com.gis.utils.presentation.BaseView
import com.gis.featuremainscreen.R
import com.gis.featuremainscreen.databinding.FragmentMainScreenBinding
import com.gis.featuremainscreen.navigation.MainScreenNavigator
import com.gis.featuremainscreen.presentation.ui.mainscreen.MainScreenIntent.ChangeTab
import com.gis.featuremainscreen.presentation.ui.mainscreen.MainTab.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreenFragment : Fragment(), BaseView<MainScreenState>,
  BackPressHandler {

  private var currentState: MainScreenState? = null
  private var binding: FragmentMainScreenBinding? = null
  private val viewModel: MainScreenViewModel by viewModel()
  private val intentsPublisher = PublishSubject.create<MainScreenIntent>()
  private lateinit var intentsSubscription: Disposable

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      initMainScreenNavigator()
    } else {
      updateMainScreenNavigator()
    }
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initBottomNavView()
    initIntents()
    return binding!!.root
  }

  override fun onDestroyView() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroyView()
  }

  private fun initMainScreenNavigator() {
    val mainScreenNavigator: MainScreenNavigator = get()
    mainScreenNavigator.setChildFragmentManager(childFragmentManager)
    mainScreenNavigator.setChildFragmentContainer(R.id.childFragmentContainer)
    mainScreenNavigator.addInnerScreens()
  }

  private fun updateMainScreenNavigator() {
    val mainScreenNavigator: MainScreenNavigator = get()
    mainScreenNavigator.setChildFragmentManager(childFragmentManager)
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_screen, container, false)
  }

  private fun initBottomNavView() {
    binding!!.bottomNavView.setOnNavigationItemSelectedListener { item ->
      intentsPublisher.onNext(
        ChangeTab(
          when (item.itemId) {
            R.id.feed -> FEED
            R.id.chart -> FRIENDS
            R.id.chat -> CHAT
            R.id.profile -> PROFILE
            else -> throw IllegalArgumentException("Unknown tab")
          }
        )
      )
      return@setOnNavigationItemSelectedListener true
    }
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(
      listOf(
        intentsPublisher
      )
    )
      .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: MainScreenState) {
    currentState = state
  }

  override fun onBackPress(): Boolean {
    val fragments = childFragmentManager.fragments
    val curFrag = fragments.find { f -> f.isResumed && !f.isHidden }
    if (curFrag is BackPressHandler && curFrag.onBackPress()) {
      return true
    }
    return false
  }
}