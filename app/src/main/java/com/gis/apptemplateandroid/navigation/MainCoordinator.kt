package com.gis.apptemplateandroid.navigation

import android.annotation.SuppressLint
import com.gis.repository.domain.entity.AppSession.LOGGED
import com.gis.repository.domain.entity.AppSession.NOT_LOGGED
import com.gis.repository.domain.session.SessionManager
import com.gis.featureauth.navigation.AuthCoordinator
import com.gis.featuremainscreen.navigation.MainScreenCoordinator
import io.reactivex.android.schedulers.AndroidSchedulers

class MainCoordinator(
  private val sessionManager: SessionManager,
  private val authCoordinator: AuthCoordinator,
  private val mainScreenCoordinator: MainScreenCoordinator
) {

  @SuppressLint("CheckResult")
  fun start() {
    sessionManager.observeSession()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { session ->
        when (session) {
          NOT_LOGGED -> authCoordinator.start()
          LOGGED -> mainScreenCoordinator.start()
        }
      }
  }
}