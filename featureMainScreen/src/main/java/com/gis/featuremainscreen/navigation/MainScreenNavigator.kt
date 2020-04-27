package com.gis.featuremainscreen.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.gis.featuremainscreen.R
import com.gis.featuremainscreen.presentation.ui.mainscreen.MainScreenFragment
import com.gis.featuremainscreen.presentation.ui.mainscreen.TabRootFragment

class MainScreenNavigator(
  private val showFeedScreen: (FragmentManager, Int) -> Unit,
  private val showChartScreen: (FragmentManager, Int) -> Unit,
  private val updateChartScreen: (FragmentManager) -> Unit,
  private val showChatScreen: (FragmentManager, Int) -> Unit,
  private val updateChatScreen: (FragmentManager) -> Unit,
  private val showProfileScreen: (FragmentManager, Int) -> Unit,
  private val updateProfileScreen: (FragmentManager) -> Unit
) {

  private var fragmentManager: FragmentManager? = null
  private var fragmentContainer: Int = -1
  private var childFragmentManager: FragmentManager? = null
  private var childFragmentContainer: Int = -1

  private var feedFragment: Fragment? = null
  private var chartFragment: Fragment? = null
  private var chatFragment: Fragment? = null
  private var profileFragment: Fragment? = null

  fun setFragmentManager(fm: FragmentManager) {
    fragmentManager = fm
  }

  fun setFragmentContainer(container: Int) {
    fragmentContainer = container
  }

  fun setChildFragmentManager(cfm: FragmentManager?) {
    childFragmentManager = cfm

    childFragmentManager?.run {

      feedFragment = findFragmentByTag("FeedFragment") ?: TabRootFragment()
      chartFragment = findFragmentByTag("ChartFragment") ?: TabRootFragment()
      chatFragment = findFragmentByTag("ChatFragment") ?: TabRootFragment()
      profileFragment = findFragmentByTag("ProfileFragment") ?: TabRootFragment()

      registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
          super.onFragmentActivityCreated(fm, f, savedInstanceState)

          when (f) {
            feedFragment -> {
            }

            chartFragment -> {
              if (chartFragment?.childFragmentManager!!.backStackEntryCount == 0) {
                showChartScreen.invoke(chartFragment!!.childFragmentManager, R.id.rootFragmentContainer)
              } else {
                updateChartScreen.invoke(chartFragment!!.childFragmentManager)
              }
            }

            chatFragment -> if (chatFragment?.childFragmentManager!!.backStackEntryCount == 0) {
              showChatScreen.invoke(chatFragment!!.childFragmentManager, R.id.rootFragmentContainer)
            } else {
              updateChatScreen.invoke(chatFragment!!.childFragmentManager)
            }

            profileFragment -> if (profileFragment?.childFragmentManager!!.backStackEntryCount == 0) {
              showProfileScreen.invoke(profileFragment!!.childFragmentManager, R.id.rootFragmentContainer)
            } else {
              updateProfileScreen.invoke(profileFragment!!.childFragmentManager)
            }
          }
        }
      }, false)
    }
  }

  fun setChildFragmentContainer(container: Int) {
    childFragmentContainer = container
  }

  fun showMainScreen() {
    removePreviousScreens(fragmentManager)
    fragmentManager
      ?.beginTransaction()
      ?.setCustomAnimations(R.anim.anim_fade_in, R.anim.anim_fade_out, R.anim.anim_fade_in, R.anim.anim_fade_out)
      ?.replace(fragmentContainer, MainScreenFragment())
      ?.commit()
  }

  fun addInnerScreens() {
    childFragmentManager?.run {
      beginTransaction()
        .add(childFragmentContainer, feedFragment!!, "FeedFragment")
        .add(childFragmentContainer, chartFragment!!, "ChartFragment")
        .add(childFragmentContainer, chatFragment!!, "ChatFragment")
        .add(childFragmentContainer, profileFragment!!, "ProfileFragment")
        .hide(chartFragment!!)
        .hide(chatFragment!!)
        .hide(profileFragment!!)
        .commit()
    }
  }

  fun showFeed() {
    childFragmentManager
      ?.beginTransaction()
      ?.hide(chartFragment!!)
      ?.hide(chatFragment!!)
      ?.hide(profileFragment!!)
      ?.show(feedFragment!!)
      ?.commit()
  }

  fun showFriends() {
    childFragmentManager
      ?.beginTransaction()
      ?.hide(feedFragment!!)
      ?.hide(chatFragment!!)
      ?.hide(profileFragment!!)
      ?.show(chartFragment!!)
      ?.commit()
  }

  fun showChat() {
    childFragmentManager
      ?.beginTransaction()
      ?.hide(feedFragment!!)
      ?.hide(chartFragment!!)
      ?.hide(profileFragment!!)
      ?.show(chatFragment!!)
      ?.commit()
  }

  fun showProfile() {
    childFragmentManager
      ?.beginTransaction()
      ?.hide(feedFragment!!)
      ?.hide(chartFragment!!)
      ?.hide(chatFragment!!)
      ?.show(profileFragment!!)
      ?.commit()
  }

  private fun removePreviousScreens(fragmentManager: FragmentManager?) {
    if (!fragmentManager?.isStateSaved!!) {
      fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
      if (fragmentManager.fragments.size != 0) {
        for (f in fragmentManager.fragments) {
          fragmentManager.beginTransaction().remove(f).commit()
        }
      }
    }
  }
}