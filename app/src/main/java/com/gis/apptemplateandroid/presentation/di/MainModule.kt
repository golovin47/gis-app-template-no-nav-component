package com.gis.apptemplateandroid.presentation.di

import com.gis.apptemplateandroid.navigation.MainCoordinator
import org.koin.dsl.module

val mainModule = module {
    single {
      MainCoordinator(
        get(),
        get(),
        get()
      )
    }
}