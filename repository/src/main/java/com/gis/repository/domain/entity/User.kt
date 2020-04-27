package com.gis.repository.domain.entity

import com.gis.utils.toDayMonthYearString
import java.util.*

data class User(
  val id: Int,
  val email: String,
  val username: String,
  val phone: String,
  val birthDate: Date,
  val avatarUrl: String,
  val fingerprintEnabled: Boolean = false
) {

  companion object {
    val EMPTY = User(
      id = -1,
      email = "",
      username = "",
      phone = "",
      birthDate = Date(),
      avatarUrl = "",
      fingerprintEnabled = false
    )
  }

  override fun toString(): String {
    return "$id,$email,$username,$phone,${birthDate.toDayMonthYearString()},$avatarUrl,$fingerprintEnabled"
  }
}