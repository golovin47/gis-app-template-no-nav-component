package com.gis.utils.presentation.ui

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlin.math.sqrt


object ShakeDetector : SensorEventListener {
  /*
	 * The gForce that is necessary to register as shake.
	 * Must be greater than 1G (one earth gravity unit).
	 * You can install "G-Force", by Blake La Pierre
	 * from the Google Play Store and run it to see how
	 *  many G's it takes to register a shake
	 */
  private val SHAKE_THRESHOLD_GRAVITY = 2.7f
  private val SHAKE_SLOP_TIME_MS = 500
  private val SHAKE_COUNT_RESET_TIME_MS = 3000

  private var observersCount: Int = 0

  private val shakeEventPublisher = PublishSubject.create<ShakeEvent>()
  private var shakeTimestamp: Long = 0
  private var shakeCount = 0

  /** Start observing shake events. Don't forget to call [stopObserve(context: Context)] */
  fun startObserve(context: Context): Observable<ShakeEvent> {
    if (observersCount == 0) {
      (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager).apply {
        registerListener(this@ShakeDetector, getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI)
      }
      observersCount++
    }
    return shakeEventPublisher
  }

  fun stopObserve(context: Context) {
    if (observersCount == 1) {
      (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager).apply {
        unregisterListener(this@ShakeDetector)
      }
      observersCount--
    }
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    // ignore
  }

  override fun onSensorChanged(event: SensorEvent) {
    val x = event.values[0]
    val y = event.values[1]
    val z = event.values[2]
    val gX = x / SensorManager.GRAVITY_EARTH
    val gY = y / SensorManager.GRAVITY_EARTH
    val gZ = z / SensorManager.GRAVITY_EARTH
    // gForce will be close to 1 when there is no movement.
    val gForce = sqrt(gX * gX + gY * gY + gZ * gZ)
    if (gForce > SHAKE_THRESHOLD_GRAVITY) {
      val now = System.currentTimeMillis()
      // ignore shake events too close to each other (500ms)
      if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
        return
      }
      // reset the shake count after 3 seconds of no shakes
      if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
        shakeCount = 0
      }
      shakeTimestamp = now
      shakeCount++
      shakeEventPublisher.onNext(ShakeEvent)
    }
  }
}

object ShakeEvent
