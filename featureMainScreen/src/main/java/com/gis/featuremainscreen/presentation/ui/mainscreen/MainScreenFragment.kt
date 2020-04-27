package com.gis.featuremainscreen.presentation.ui.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gis.featuremainscreen.R
import com.gis.featuremainscreen.databinding.FragmentMainScreenBinding
import com.gis.featuremainscreen.navigation.MainScreenNavigator
import com.gis.featuremainscreen.presentation.ui.mainscreen.MainScreenIntent.ChangeTab
import com.gis.featuremainscreen.presentation.ui.mainscreen.MainTab.*
import com.gis.utils.presentation.BackPressHandler
import com.gis.utils.presentation.BaseMviFragment
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreenFragment :
  BaseMviFragment<MainScreenState, FragmentMainScreenBinding, MainScreenViewModel>(),
  BackPressHandler {

  override val layoutId: Int = R.layout.fragment_main_screen
  override val viewModel: MainScreenViewModel by viewModel()
  private val intentsPublisher = PublishSubject.create<MainScreenIntent>()
  private var currentState: MainScreenState? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      initMainScreenNavigator()
    } else {
      updateMainScreenNavigator()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    initBottomNavView()
    return binding!!.root
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

  override fun userIntents(): Observable<Any> =
    Observable.merge(
      listOf(
        intentsPublisher
      )
    )

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