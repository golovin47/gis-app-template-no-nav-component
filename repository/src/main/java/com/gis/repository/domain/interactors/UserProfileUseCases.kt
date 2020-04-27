package com.gis.repository.domain.interactors

import androidx.fragment.app.FragmentActivity
import com.gis.repository.domain.entity.User
import com.gis.repository.domain.repository.UserRepository
import com.gis.repository.domain.service.ChangePasswordService


class ObserveUserUseCase(private val userRepository: UserRepository) {
  fun execute() = userRepository.observeUser()
}


class ShowFingerprintDialogAndCheckUsers(private val userRepository: UserRepository) {
  fun execute(activity: FragmentActivity) = userRepository.showFingerprintDialogAndCheckUsers(activity)
}


class GetFingerprintUsers(private val userRepository: UserRepository) {
  fun execute() = userRepository.getFingerprintUsers()
}


class UpdateUserUseCase(private val userRepository: UserRepository) {
  fun execute(user: User) = userRepository.updateUser(user)
}


class PutUserUseCase(private val userRepository: UserRepository) {
  fun execute(user: User) = userRepository.putUser(user)
}


class ChangePasswordUseCase(private val changePasswordService: ChangePasswordService) {
  fun execute(oldPassword: String, newPassword: String) =
    changePasswordService.changePassword(oldPassword, newPassword)
}


class EnableFingerprintAuthUseCase(private val userRepository: UserRepository) {
  fun execute(enable: Boolean) = userRepository.enableFingerprintAuth(enable)
}