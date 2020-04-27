package com.gis.repository.data.local.db

import android.content.Context
import androidx.room.Room

object AppTemplateDbProvider {
  fun createDb(context: Context) =
    Room.databaseBuilder(context, AppTemplateDb::class.java, "AppTemplateDb")
      .allowMainThreadQueries()
      .build()
}