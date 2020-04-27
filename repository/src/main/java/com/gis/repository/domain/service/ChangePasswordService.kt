package com.gis.repository.domain.service

import io.reactivex.Completable

interface ChangePasswordService {

  fun changePassword(oldPassword: String, newPassword: String): Completable
}