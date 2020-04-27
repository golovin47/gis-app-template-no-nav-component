package com.gis.repository.data.service

import com.gis.repository.domain.service.CountdownService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class CountdownServiceImpl : CountdownService {

  private lateinit var countdownSubscription: Disposable
  private val countdownPublisher = BehaviorSubject.create<Int>()

  override fun observeCountdown(): Observable<Int> = countdownPublisher

  override fun startCountdown(): Completable = Completable.fromAction {
    initiateCountdown()
  }

  private fun initiateCountdown() {
    if (this::countdownSubscription.isInitialized) {
      countdownSubscription.dispose()
    }

    val observable = Observable.interval(0,1, TimeUnit.SECONDS)
      .take(61)
      .map { 60 - it.toInt() }
      .doOnNext { countdownPublisher.onNext(it) }

    countdownSubscription = observable.subscribe()
  }


  override fun finsihCountdown(): Completable = Completable.fromAction {
    countdownPublisher.onNext(-1)
  }
}

