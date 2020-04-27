package com.gis.repository.data.service

import com.gis.repository.domain.service.ChangePasswordService
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

class ChangePasswordServiceImpl() : ChangePasswordService {

  //TODO add API to constructor

  override fun changePassword(oldPassword: String, newPassword: String): Completable =
    Completable.timer(3000, TimeUnit.MILLISECONDS) //TODO change to API call
}