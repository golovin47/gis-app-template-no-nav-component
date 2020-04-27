package com.gis.repository.data.local.db

import androidx.room.TypeConverter
import com.bluelinelabs.logansquare.LoganSquare
import com.gis.utils.toSimpleDate
import com.gis.utils.toDayMonthYearString
import java.util.*

class DbTypeConverters {

  @TypeConverter
  fun fromStringToList(value: String): List<String> =
    LoganSquare.parseList(value, String::class.java)

  @TypeConverter
  fun fromListToString(list: List<String>): String = LoganSquare.serialize(list)

  @TypeConverter
  fun fromStringToDate(dateString: String): Date = dateString.toSimpleDate()

  @TypeConverter
  fun fromDateToString(date: Date): String = date.toDayMonthYearString()
}