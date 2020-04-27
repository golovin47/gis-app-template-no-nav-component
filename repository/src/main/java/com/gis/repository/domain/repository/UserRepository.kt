package com.gis.repository.domain.repository

import androidx.fragment.app.FragmentActivity
import com.gis.repository.domain.entity.User
import io.reactivex.Completable
import io.reactivex.Observable

interface UserRepository {

  fun observeUser(): Observable<User>

  fun showFingerprintDialogAndCheckUsers(activity: FragmentActivity): Observable<List<User>>

  fun getFingerprintUsers(): Observable<List<User>>

  fun putUser(user: User): Completable

  fun updateUser(user: User): Completable

  fun enableFingerprintAuth(enable: Boolean): Completable

  fun clear(): Completable
}