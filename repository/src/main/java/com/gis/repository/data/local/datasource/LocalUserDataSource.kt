package com.gis.repository.data.local.datasource

import android.content.SharedPreferences
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.gis.repository.domain.datasource.UserDataSource
import com.gis.repository.domain.entity.User
import com.gis.utils.toSimpleDate
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.Executors

class LocalUserDataSource(private val sharedPrefs: SharedPreferences) : UserDataSource {

  private val userPublisher =
    BehaviorSubject.createDefault<User>(sharedPrefs.getString("user", User.EMPTY.toString())!!.toUser())

  override fun getUser(): Observable<User> = userPublisher

  override fun showFingerprintDialogAndCheckUsers(activity: FragmentActivity): Observable<List<User>> =
    Observable.create { emitter ->
      val biometricPrompt =
        BiometricPrompt(activity, Executors.newSingleThreadExecutor(), biometricAuthCallback(emitter))
      val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Use your fingerprint to authorize")
        .setNegativeButtonText("Cancel")
        .build()
      biometricPrompt.authenticate(promptInfo)
    }

  override fun getFingerprintUsers(): Observable<List<User>> =
    Observable.create { emitter ->
      val users = sharedPrefs.getStringSet("users", mutableSetOf())!!.map { userString -> userString.toUser() }
      if (users.isNotEmpty()) {
        emitter.onNext(users)
      } else {
        emitter.onError(Throwable("No users available for fingerprint auth on this device"))
      }
    }

  override fun putUser(user: User): Completable =
    Completable.fromCallable { sharedPrefs.edit().putString("user", user.toString()).apply() }
      .doOnComplete { userPublisher.onNext(user) }

  override fun updateUser(user: User): Completable =
    Completable.fromCallable { sharedPrefs.edit().putString("user", user.toString()).apply() }
      .doOnComplete { userPublisher.onNext(user) }

  override fun enableFingerprintAuth(enable: Boolean): Completable =
    Completable.fromCallable {
      val curUser = userPublisher.value!!
      val usersSet = sharedPrefs.getStringSet("users", mutableSetOf())!!.apply {
        if (enable) {
          add(curUser.copy(fingerprintEnabled = true).toString())
        } else {
          val userStringToRemove = find { userString ->
            val user = userString.toUser()
            user.username == curUser.username || user.phone == curUser.phone || user.email == curUser.email
          }
          if (userStringToRemove != null) {
            remove(userStringToRemove)
          }
        }
      }
      sharedPrefs.edit().putStringSet("users", usersSet).apply()
      sharedPrefs.edit().putString("user", userPublisher.value!!.copy(fingerprintEnabled = enable).toString()).apply()
    }
      .doOnComplete { userPublisher.onNext(userPublisher.value!!.copy(fingerprintEnabled = enable)) }

  override fun clear(): Completable =
    Completable.fromCallable {
      sharedPrefs.edit().remove("user").apply()
    }
      .doOnComplete { userPublisher.onNext(User.EMPTY) }

  private val biometricAuthCallback: (ObservableEmitter<List<User>>) -> BiometricPrompt.AuthenticationCallback =
    { emitter ->
      object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
          super.onAuthenticationError(errorCode, errString)
          if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON && errorCode != BiometricPrompt.ERROR_USER_CANCELED) {
            emitter.onError(Throwable("Biometric auth error. Code: $errorCode Message: $errString"))
          } else {
            emitter.onComplete()
          }
        }

        override fun onAuthenticationFailed() {
          super.onAuthenticationFailed()
          emitter.onError(Throwable("Biometric auth failed"))
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
          super.onAuthenticationSucceeded(result)
          val users = sharedPrefs.getStringSet("users", mutableSetOf())!!.map { userString -> userString.toUser() }
          if (users.isNotEmpty()) {
            emitter.onNext(users)
          } else {
            emitter.onError(Throwable("No users available for fingerprint auth on this device"))
          }
        }
      }
    }

  private fun String.toUser(): User {
    val stringValues = split(",")
    return if (stringValues[0].toInt() == -1) User.EMPTY
    else User(
      id = stringValues[0].toInt(),
      email = stringValues[1],
      username = stringValues[2],
      phone = stringValues[3],
      birthDate = stringValues[4].toSimpleDate(),
      avatarUrl = stringValues[5],
      fingerprintEnabled = stringValues[6].toBoolean()
    )
  }
}