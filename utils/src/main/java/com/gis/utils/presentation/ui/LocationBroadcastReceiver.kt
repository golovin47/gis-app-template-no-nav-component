package com.gis.utils.presentation.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import io.reactivex.subjects.BehaviorSubject

class LocationBroadcastReceiver : BroadcastReceiver() {

    private val locationStatePublisher = BehaviorSubject.create<LocationState>()

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (isGpsEnabled || isNetworkEnabled) { //location is enabled
                locationStatePublisher.onNext(LocationState.LOCATION_ENABLED)
            } else { //location is disabled
                locationStatePublisher.onNext(LocationState.LOCATION_DISABLED)
            }
        }
    }

    fun observeLocationState() = locationStatePublisher

    enum class LocationState {
        LOCATION_ENABLED,
        LOCATION_DISABLED
    }
}