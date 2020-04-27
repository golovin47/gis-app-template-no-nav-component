package com.gis.featureonboarding.presentation.onboardingroot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.gis.utils.presentation.BaseView
import com.gis.featureonboarding.R
import com.gis.featureonboarding.databinding.FragmentOnboardingRootBinding
import com.gis.featureonboarding.presentation.AnimationPage
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.gis.featureonboarding.presentation.onboardingroot.OnboardingRootIntent.*
import org.koin.core.qualifier.named

class OnboardingRootFragment : Fragment(), BaseView<OnboardingRootState> {

  private var binding: FragmentOnboardingRootBinding? = null
  private lateinit var currentState: OnboardingRootState
  private lateinit var intentsSubscription: Disposable
  private val intentsPublisher = PublishSubject.create<OnboardingRootIntent>()
  private val viewModel: OnboardingRootViewModel by viewModel()

  private lateinit var firstPage: AnimationPage
  private lateinit var secondPage: AnimationPage
  private lateinit var thirdPage: AnimationPage

  private val showGotItButton: Boolean by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getBoolean("showGotItButton") }

  companion object {
    fun getInstance(showGotItButton: Boolean) =
      OnboardingRootFragment().apply {
        arguments = Bundle().apply {
          putBoolean("showGotItButton", showGotItButton)
        }
      }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleStates()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    initOnboardingNavigator()
    initPages()
    initOnboardingViewPager()
    initIntents()
    return binding!!.root
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding_root, container, false)
  }

  private fun initOnboardingNavigator() {
    get<(FragmentManager) -> Unit>(named("setOnboardingChildFragmentManager")).invoke(childFragmentManager)
  }

  private fun initPages() {
    firstPage = get(named("getFirstOnboardingPage"))
    secondPage = get(named("getSecondOnboardingPage"))
    thirdPage = get<(Boolean) -> AnimationPage>(named("getThirdOnboardingPage")).invoke(showGotItButton)
  }

  private fun initOnboardingViewPager() {
    binding!!.vpOnboarding.offscreenPageLimit = 2
    binding!!.vpOnboarding.adapter =
      OnboardingViewPagerAdapter(
        childFragmentManager,
        listOf(firstPage, secondPage, thirdPage)
      )

    binding!!.vpOnboarding.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
      override fun onPageScrollStateChanged(state: Int) {}

      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

      override fun onPageSelected(position: Int) {
        if (this@OnboardingRootFragment::currentState.isInitialized && position != currentState.curPage) {
          intentsPublisher.onNext(ChangeCurPage(position))
          when (position) {
            FIRST_ONBOARDING_PAGE -> firstPage.playAnimation()
            SECOND_ONBOARDING_PAGE -> secondPage.playAnimation()
            THIRD_ONBOARDING_PAGE -> thirdPage.playAnimation()
          }
        }
      }
    })
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

  override fun render(state: OnboardingRootState) {
    currentState = state

    if (state.showInitAnimation) {
      firstPage.playAnimation()
    }
  }
}