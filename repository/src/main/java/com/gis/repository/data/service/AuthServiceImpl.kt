package com.gis.repository.data.service

import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.gis.repository.domain.entity.User
import com.gis.repository.domain.repository.UserRepository
import com.gis.repository.domain.service.AuthService
import com.jaychang.sa.AuthCallback
import com.jaychang.sa.SocialUser
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private typealias FacebookAuth = com.jaychang.sa.facebook.SimpleAuth
private typealias TwitterAuth = com.jaychang.sa.twitter.SimpleAuth
private typealias InstagramAuth = com.jaychang.sa.instagram.SimpleAuth
private typealias GoogleAuth = com.jaychang.sa.google.SimpleAuth

class AuthServiceImpl(private val userRepository: UserRepository) : AuthService {
  //TODO add API to constructor

  override fun signUpWithCreds(email: String, username: String, password: String): Completable =
    Observable.timer(3000, TimeUnit.MILLISECONDS) //TODO add API call instead
      .map { User(id = 1, email = email, username = username, avatarUrl = "", phone = "", birthDate = Date()) }
      .putUserCompletable()

  override fun signInWithCreds(email: String, password: String): Completable =
    Observable.timer(3000, TimeUnit.MILLISECONDS) //TODO add API call instead
      .map { User(id = 1, email = email, username = "", avatarUrl = "", phone = "", birthDate = Date()) }
      .putUserCompletable()

  override fun authWithPhone(phone: String, code: String): Completable =
    Observable.timer(3000, TimeUnit.MILLISECONDS) //TODO add API call instead
      .map { User(id = 1, email = "", username = "", avatarUrl = "", phone = phone, birthDate = Date()) }
      .putUserCompletable()

  override fun authWithFingerprint(user: User): Completable = userRepository.putUser(user)

  override fun authWithFacebook(): Completable =
    Observable.create(ObservableOnSubscribe<User> { emitter ->
      FacebookAuth.connectFacebook(listOf("email"), socialAuthCallback.invoke(emitter))
    })                                                   //TODO add API call to chain
      .putUserCompletable()

  override fun authWithTwitter(): Completable =
    Observable.create(ObservableOnSubscribe<User> { emitter ->
      TwitterAuth.connectTwitter(socialAuthCallback.invoke(emitter))
    })                                                   //TODO add API call to chain
      .putUserCompletable()

  override fun authWithInstagram(): Completable =
    Observable.create(ObservableOnSubscribe<User> { emitter ->
      InstagramAuth.connectInstagram(listOf("basic"), socialAuthCallback.invoke(emitter))
    })
      .putUserCompletable()

  override fun authWithGoogle(): Completable =
    Observable.create(ObservableOnSubscribe<User> { emitter ->
      GoogleAuth.connectGoogle(listOf("profile"), socialAuthCallback.invoke(emitter))
    })
      .putUserCompletable()

  override fun signOut(): Completable = userRepository.clear()

  private val socialAuthCallback: (ObservableEmitter<User>) -> AuthCallback =
    { emitter ->
      object : AuthCallback {
        override fun onSuccess(socialUser: SocialUser) {
          emitter.onNext(socialUser.toUser())
        }

        override fun onCancel() {
          emitter.onComplete()
        }

        override fun onError(error: Throwable) {
          emitter.onError(error)
        }
      }
    }

  private fun Observable<User>.putUserCompletable(): Completable =
    flatMapCompletable { user ->
      userRepository.putUser(user)
    }

  private fun SocialUser.toUser(): User =
    User(
      id = 1,
      email = this.email,
      username = this.username,
      avatarUrl = this.profilePictureUrl,
      phone = "",
      birthDate = Date()
    )
}