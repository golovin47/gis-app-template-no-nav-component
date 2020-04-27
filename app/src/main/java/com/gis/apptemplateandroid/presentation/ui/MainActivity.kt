package com.gis.apptemplateandroid.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gis.utils.presentation.BackPressHandler
import com.gis.apptemplateandroid.R
import com.gis.apptemplateandroid.navigation.MainCoordinator
import com.gis.featureauth.navigation.AuthNavigator
import com.gis.featuremainscreen.navigation.MainScreenNavigator
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

  private val mainCoordinator: MainCoordinator by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    get<AuthNavigator>().apply {
      setFragmentManager(supportFragmentManager)
      setFragmentContainer(R.id.fragmentContainer)
    }

    get<MainScreenNavigator>().apply {
      setFragmentManager(supportFragmentManager)
      setFragmentContainer(R.id.fragmentContainer)
    }

    if (savedInstanceState == null)
      mainCoordinator.start()
  }

  override fun onBackPressed() {
    val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
    if (fragment is BackPressHandler && fragment.onBackPress())
      return
    super.onBackPressed()
  }
}
