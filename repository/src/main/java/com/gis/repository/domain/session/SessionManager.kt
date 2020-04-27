package com.gis.repository.domain.session

import com.gis.repository.domain.entity.AppSession
import io.reactivex.Observable

interface SessionManager {
  fun observeSession(): Observable<AppSession>
}