package com.gis.utils.di

import com.gis.utils.createTempFileForPhotoAndGetUri
import com.gis.utils.data.GlideImageLoader
import com.gis.utils.domain.ImageLoader
import com.gis.utils.getAllImagesFromGallery
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

val utilsModule = module {

  single<ImageLoader> { GlideImageLoader }

  single { PhoneNumberUtil.createInstance(androidApplication()) }

  factory(named("createTempFileForPhotoAndGetUri")) { { createTempFileForPhotoAndGetUri(androidApplication()) } }

  factory(named("getAllGalleryImages")) { { getAllImagesFromGallery(androidApplication()) } }
}