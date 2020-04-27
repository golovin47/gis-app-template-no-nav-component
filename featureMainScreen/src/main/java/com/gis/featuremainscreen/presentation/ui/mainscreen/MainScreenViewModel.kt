package com.gis.featuremainscreen.presentation.ui.mainscreen

import com.gis.utils.presentation.BaseViewModel
import com.gis.featuremainscreen.presentation.ui.mainscreen.MainScreenIntent.ChangeTab
import io.reactivex.Observable
import com.gis.featuremainscreen.presentation.ui.mainscreen.MainTab.*
import com.gis.featuremainscreen.presentation.ui.mainscreen.MainScreenStateChange.TabChanged

class MainScreenViewModel(
  private var openFeed: (() -> Unit)?,
  private var openFriends: (() -> Unit)?,
  private var openChat: (() -> Unit)?,
  private var openProfile: (() -> Unit)?
) : BaseViewModel<MainScreenState>() {

  override fun initState(): MainScreenState = MainScreenState()

  override fun viewIntents(intentStream: Observable<*>): Observable<Any> =
    Observable.merge(
      listOf(
        intentStream.ofType(ChangeTab::class.java)
          .doOnNext { event ->
            when (event.tab) {
              FEED -> openFeed?.invoke()
              FRIENDS -> openFriends?.invoke()
              CHAT -> openChat?.invoke()
              PROFILE -> openProfile?.invoke()
            }
          }
      )
    )

  override fun reduceState(previousState: MainScreenState, stateChange: Any): MainScreenState =
    when (stateChange) {
      is TabChanged -> previousState.copy(curTab = stateChange.tab)
      else -> previousState
    }

  override fun onCleared() {
    openFeed = null
    openFriends = null
    openChat = null
    openProfile = null
    super.onCleared()
  }
}