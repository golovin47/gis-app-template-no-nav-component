package com.gis.featureonboarding.presentation.onboarding

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.gis.featureonboarding.R
import com.gis.featureonboarding.databinding.FragmentOnboardingBinding
import com.gis.featureonboarding.presentation.AnimationPage
import org.koin.android.ext.android.get
import org.koin.core.qualifier.named

class OnboardingFragment : Fragment(), AnimationPage {

  private val titleRes: Int by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getInt("titleRes") }
  private val iconRes: Int by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getInt("iconRes") }
  private val showGotItButton: Boolean by lazy(LazyThreadSafetyMode.NONE) { arguments!!.getBoolean("showGotItButton") }

  private var binding: FragmentOnboardingBinding? = null

  companion object {
    fun getInstance(titleRes: Int, iconRes: Int, showGotItButton: Boolean) =
      OnboardingFragment().apply {
        arguments = Bundle().apply {
          putInt("titleRes", titleRes)
          putInt("iconRes", iconRes)
          putBoolean("showGotItButton", showGotItButton)
        }
      }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    initBinding(inflater, container)
    return binding!!.root
  }

  private fun initBinding(inflater: LayoutInflater, container: ViewGroup?) {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_onboarding, container, false)
    binding!!.tvMainTitle.setText(titleRes)
    binding!!.lavMainIcon.setAnimation(iconRes)
    binding!!.btnGotIt.visibility = if (showGotItButton) VISIBLE else GONE
    binding!!.btnGotIt.setOnClickListener {
      get<SharedPreferences>().edit().putBoolean("onboardingPassed", true).apply()
      get<() -> Unit>(named("startAuthWithPhone")).invoke()
    }
  }

  override fun playAnimation() {
    binding?.lavMainIcon?.playAnimation()
  }

  override fun pauseAnimation() {
    binding?.lavMainIcon?.pauseAnimation()
  }

  override fun animateTo(to: Float) {
    binding?.lavMainIcon?.progress = to
  }
}