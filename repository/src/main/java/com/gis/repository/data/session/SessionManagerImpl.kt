package com.gis.repository.data.session

import android.annotation.SuppressLint
import com.gis.repository.domain.entity.AppSession
import com.gis.repository.domain.entity.AppSession.LOGGED
import com.gis.repository.domain.entity.AppSession.NOT_LOGGED
import com.gis.repository.domain.entity.User
import com.gis.repository.domain.repository.UserRepository
import com.gis.repository.domain.session.SessionManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class SessionManagerImpl(private val userRepository: UserRepository) :
  SessionManager {

  private val sessionPublisher = BehaviorSubject.createDefault(NOT_LOGGED)

  @SuppressLint("CheckResult")
  override fun observeSession(): Observable<AppSession> {
    userRepository.observeUser().subscribe { user ->
      if (sessionPublisher.value != NOT_LOGGED && user == User.EMPTY) {
        sessionPublisher.onNext(NOT_LOGGED)
      }
      if (sessionPublisher.value != LOGGED && user != User.EMPTY) {
        sessionPublisher.onNext(LOGGED)
      }
    }
    return sessionPublisher
  }
}