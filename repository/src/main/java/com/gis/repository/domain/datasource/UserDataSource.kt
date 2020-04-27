package com.gis.repository.domain.datasource

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.gis.repository.domain.entity.User
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.Executors

interface UserDataSource {

  fun getUser(): Observable<User>

  fun showFingerprintDialogAndCheckUsers(activity: FragmentActivity): Observable<List<User>>

  fun getFingerprintUsers(): Observable<List<User>>

  fun putUser(user: User): Completable

  fun updateUser(user: User): Completable

  fun enableFingerprintAuth(enable: Boolean): Completable

  fun clear(): Completable
}