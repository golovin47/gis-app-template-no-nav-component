package com.gis.apptemplateandroid.application

import android.app.Application
import com.gis.repository.di.repoModule
import com.gis.utils.di.utilsModule
import com.gis.apptemplateandroid.presentation.di.mainModule
import com.gis.featureauth.di.authModule
import com.gis.featureauthwithphone.di.authWithPhoneModule
import com.gis.featurechart.di.chartModule
import com.gis.featurechat.di.chatModule
import com.gis.featurechoosecountry.di.chooseCountryModule
import com.gis.featuremainscreen.di.mainScreenModule
import com.gis.featureonboarding.di.onboardingModule
import com.gis.featureuserprofile.di.userProfileModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppTemplateAndroid : Application() {

  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@AppTemplateAndroid)
      modules(listOf(
        mainModule,
        authModule,
        authWithPhoneModule,
        onboardingModule,
        chooseCountryModule,
        mainScreenModule,
        chartModule,
        chatModule,
        userProfileModule,
        repoModule,
        utilsModule
      ))
    }
  }
}