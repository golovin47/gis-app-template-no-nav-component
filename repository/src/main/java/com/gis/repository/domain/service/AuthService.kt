package com.gis.repository.domain.service

import androidx.fragment.app.FragmentActivity
import com.gis.repository.domain.entity.User
import io.reactivex.Completable

interface AuthService {

  fun signUpWithCreds(email: String, username: String, password: String): Completable

  fun signInWithCreds(email: String, password: String): Completable

  fun authWithPhone(phone: String, code: String): Completable

  fun authWithFingerprint(user: User): Completable

  fun authWithFacebook(): Completable

  fun authWithTwitter(): Completable

  fun authWithInstagram(): Completable

  fun authWithGoogle(): Completable

  fun signOut(): Completable
}