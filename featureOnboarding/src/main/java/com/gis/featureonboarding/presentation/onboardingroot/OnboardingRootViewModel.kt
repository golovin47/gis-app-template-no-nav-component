package com.gis.featureonboarding.presentation.onboardingroot

import com.gis.utils.presentation.BaseViewModel
import io.reactivex.Observable
import com.gis.featureonboarding.presentation.onboardingroot.OnboardingRootIntent.*
import com.gis.featureonboarding.presentation.onboardingroot.OnboardingRootStateChange.*

class OnboardingRootViewModel : BaseViewModel<OnboardingRootState>() {

  override fun initState(): OnboardingRootState = OnboardingRootState()

  override fun vmIntents(): Observable<Any> =
    Observable.merge(
      listOf(
        Observable.just(ShowInitAnimation, InitAnimationShown)
      )
    )

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(
        intentStream.ofType(ChangeCurPage::class.java)
          .map { CurPageChanged(it.page) }
      )
    )

  override fun reduceState(previousState: OnboardingRootState, stateChange: Any): OnboardingRootState =
    when (stateChange) {
      is CurPageChanged -> previousState.copy(curPage = stateChange.page)
      is ShowInitAnimation -> previousState.copy(showInitAnimation = true)
      is InitAnimationShown -> previousState.copy(showInitAnimation = false)
      else -> previousState
    }
}