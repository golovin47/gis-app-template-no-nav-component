package com.gis.repository.domain.service

import io.reactivex.Completable
import io.reactivex.Observable

interface CountdownService {

  fun startCountdown(): Completable

  fun finsihCountdown(): Completable

  fun observeCountdown(): Observable<Int>
}