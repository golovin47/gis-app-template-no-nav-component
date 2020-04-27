package com.gis.repository.data.repository

import androidx.fragment.app.FragmentActivity
import com.gis.repository.domain.datasource.UserDataSource
import com.gis.repository.domain.entity.User
import com.gis.repository.domain.repository.UserRepository
import io.reactivex.Completable
import io.reactivex.Observable

class UserRepositoryImpl(private val localUserDataSource: UserDataSource) : UserRepository {

  override fun observeUser(): Observable<User> = localUserDataSource.getUser()

  override fun showFingerprintDialogAndCheckUsers(activity: FragmentActivity): Observable<List<User>> =
    localUserDataSource.showFingerprintDialogAndCheckUsers(activity)

  override fun getFingerprintUsers(): Observable<List<User>> = localUserDataSource.getFingerprintUsers()

  override fun putUser(user: User): Completable = localUserDataSource.putUser(user)

  override fun updateUser(user: User): Completable = localUserDataSource.updateUser(user)

  override fun enableFingerprintAuth(enable: Boolean): Completable = localUserDataSource.enableFingerprintAuth(enable)

  override fun clear(): Completable = localUserDataSource.clear()
}