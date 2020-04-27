package com.gis.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.Context.*
import android.content.Intent.*
import android.content.res.Resources
import android.database.MergeCursor
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Vibrator
import android.provider.MediaStore.Images
import android.provider.MediaStore.MediaColumns
import android.provider.OpenableColumns
import android.provider.Settings
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.FileProvider
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import io.reactivex.Observable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import android.os.VibrationEffect.*
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.*
import android.net.NetworkCapabilities.*
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.core.content.res.ResourcesCompat
import com.gis.utils.presentation.ui.FontSpan


val Int.toDp: Int
  get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.toPx: Int
  get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Resources.getStatusBarHeight(): Int {
  val id = getIdentifier("status_bar_height", "dimen", "android")
  return if (id != 0) getDimensionPixelSize(id) else 0
}

fun Resources.getNavBarHeight(): Int {
  val id = getIdentifier("navigation_bar_height", "dimen", "android")
  return if (id != 0) getDimensionPixelSize(id) else 0
}

fun Int.bytesToKb(): Int =
  when {
    this < 0 -> 0
    this == 0 -> 0
    else -> Math.ceil(this.toDouble() / 1024.toDouble()).roundToInt()
  }

fun Long.bytesToKb(): Long =
  when {
    this < 0 -> 0
    this == 0L -> 0
    else -> Math.ceil(this.toDouble() / 1024.toDouble()).roundToLong()
  }

fun Date.toDayMonthYearString(): String {
  val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
  return sdf.format(this)
}

fun Date.toTimeString(): String {
  val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
  return sdf.format(this)
}

fun Date.toChatMessageTime(): String {
  val today = Calendar.getInstance()
  val curDate = Calendar.getInstance().apply { time = this@toChatMessageTime }

  val isToday = today.get(Calendar.YEAR) == curDate.get(Calendar.YEAR) &&
      today.get(Calendar.DAY_OF_YEAR) == curDate.get(Calendar.DAY_OF_YEAR)

  return if (isToday) this.toTimeString() else this.toDayMonthYearString()
}

fun String.toSimpleDate(): Date {
  val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
  return sdf.parse(this)
}

fun getAllImagesFromGallery(context: Context): Observable<List<String>> =
  Observable.fromCallable {
    val projection = arrayOf(MediaColumns.DATA, Images.Media.BUCKET_DISPLAY_NAME)
    val innerCursor = context.contentResolver.query(
      Images.Media.INTERNAL_CONTENT_URI,
      projection,
      null,
      null,
      Images.Media.DEFAULT_SORT_ORDER
    )
    val externalCursor = context.contentResolver.query(
      Images.Media.EXTERNAL_CONTENT_URI,
      projection,
      null,
      null,
      Images.Media.DEFAULT_SORT_ORDER
    )
    val cursor = MergeCursor(arrayOf(innerCursor, externalCursor))
    val columnIndexData = cursor.getColumnIndexOrThrow(MediaColumns.DATA)

    val images = mutableListOf<String>()
    while (cursor.moveToNext()) {
      images.add(cursor.getString(columnIndexData))
    }

    return@fromCallable images
  }

fun createTempFileForPhotoAndGetUri(context: Context): Observable<Uri> =
  Observable.fromCallable {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val fileName = "PhotoFilterImg_" + timestamp + "_"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File.createTempFile(fileName, ".jpg", storageDir)
    FileProvider.getUriForFile(context, "com.gis.apptemplateandroid.provider", file)
  }

fun Context.getFileNameAndSize(uri: Uri): Pair<String, String> {
  val cursor = contentResolver?.query(uri, null, null, null, null)
  var displayName = ""
  var size = ""
  if (cursor?.moveToFirst() == true) {
    displayName =
      if (cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)).isNotEmpty())
        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
      else
        uri.host ?: "File"
    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
    size = if (!cursor.isNull(sizeIndex)) cursor.getString(sizeIndex) else "Unknown"
    cursor.close()
  }
  return Pair<String, String>(displayName, size)
}

@SuppressLint("MissingPermission")
fun Context.playVibration(time: Long) {
  val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
  if (Build.VERSION.SDK_INT >= 26)
    vibrator.vibrate(createOneShot(time, DEFAULT_AMPLITUDE))
  else
    vibrator.vibrate(time)
}

fun View.hideKeyboard() {
  val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
  val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun goToAppSettings(context: Context) {
  val intent = Intent().apply {
    addFlags(FLAG_ACTIVITY_NEW_TASK)
    action = ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", context.packageName, null)
    data = uri
  }
  context.startActivity(intent)
}

fun goToBluetoothSettings(context: Context) {
  val intent = Intent(Intent.ACTION_MAIN, null).apply {
    addFlags(FLAG_ACTIVITY_NEW_TASK)
    addCategory(Intent.CATEGORY_LAUNCHER)
    val cn = ComponentName(
      "com.android.settings",
      "com.android.settings.bluetooth.BluetoothSettings"
    )
    component = cn
  }
  context.startActivity(intent)
}

fun goToNfcSettings(context: Context) {
  val intent = Intent(Settings.ACTION_NFC_SETTINGS).apply {
    addFlags(FLAG_ACTIVITY_NEW_TASK)
  }
  context.startActivity(intent)
}

fun goToLocationSettings(context: Context) {
  val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
    addFlags(FLAG_ACTIVITY_NEW_TASK)
  }
  context.startActivity(intent)
}

fun openInGoogleMaps(
  context: Context,
  locationName: String,
  address: String,
  latitude: String,
  longitude: String
) {
  val uri = Uri.parse("geo:$latitude,$longitude?q=$locationName, $address")
  val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
    addFlags(FLAG_ACTIVITY_NEW_TASK)
    setPackage("com.google.android.apps.maps")
  }
  if (mapIntent.resolveActivity(context.packageManager) != null) {
    context.startActivity(mapIntent);
  }
}

fun copyToClipboard(context: Context, text: String) {
  val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
  val clipData = ClipData.newPlainText(text, text)
  clipboard.setPrimaryClip(clipData)
}

fun makeCall(context: Context, phone: String) {
  val intent = Intent(ACTION_DIAL).apply {
    addFlags(FLAG_ACTIVITY_NEW_TASK)
    data = Uri.parse("tel:$phone")
  }
  context.startActivity(intent)
}

@SuppressLint("MissingPermission")
fun getNetworkConnectionType(context: Context): String {
  val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

  var wifi = false
  var mobile = false

  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    val activeNetwork = cm.activeNetwork
    if (activeNetwork != null) {
      val networkCapabilities = cm.getNetworkCapabilities(activeNetwork)
      if (networkCapabilities != null) {
        wifi = networkCapabilities.hasTransport(TRANSPORT_WIFI) ||
            networkCapabilities.hasTransport(TRANSPORT_VPN)
        mobile = networkCapabilities.hasTransport(TRANSPORT_CELLULAR)
      }
    }
  } else {
    val networkInfo = cm.activeNetworkInfo
    if (networkInfo != null) {
      wifi = networkInfo.isConnected && networkInfo.type == TYPE_WIFI
      mobile = networkInfo.isConnected && networkInfo.type == TYPE_MOBILE
    }
  }

  return when {
    wifi -> context.getString(R.string.network_wifi)
    mobile -> getMobileNetType(context)
    else -> context.getString(R.string.network_no_connection)
  }
}

fun getMobileNetType(context: Context): String {
  val tm = context.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
  return when (tm.networkType) {
    NETWORK_TYPE_GPRS,
    NETWORK_TYPE_EDGE,
    NETWORK_TYPE_CDMA,
    NETWORK_TYPE_1xRTT,
    NETWORK_TYPE_IDEN -> context.getString(R.string.network_2g)

    NETWORK_TYPE_UMTS,
    NETWORK_TYPE_EVDO_0,
    NETWORK_TYPE_EVDO_A,
    NETWORK_TYPE_HSDPA,
    NETWORK_TYPE_HSUPA,
    NETWORK_TYPE_HSPA,
    NETWORK_TYPE_EVDO_B,
    NETWORK_TYPE_EHRPD,
    NETWORK_TYPE_HSPAP -> context.getString(R.string.network_3g)

    NETWORK_TYPE_LTE -> context.getString(R.string.network_4g)

    else -> context.getString(R.string.network_unknown)
  }
}

@SuppressLint("MissingPermission")
fun isFingerprintAvailable(context: Context) =
  FingerprintManagerCompat.from(context).isHardwareDetected && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

fun SpannableStringBuilder.applyFont(
  context: Context,
  boldFontResId: Int,
  italicFontResId: Int
): SpannableStringBuilder {
  val boldParts: MutableList<Pair<Int, Int>> = mutableListOf()
  val italicParts: MutableList<Pair<Int, Int>> = mutableListOf()

  val spans = getSpans(0, length, StyleSpan::class.java)
  if (spans.isNotEmpty()) {
    for (span in spans) {
      if (span.style == Typeface.BOLD) {
        boldParts.add(Pair(getSpanStart(span), getSpanEnd(span)))
      }

      if (span.style == Typeface.ITALIC) {
        italicParts.add(Pair(getSpanStart(span), getSpanEnd(span)))
      }
    }

    if (boldParts.isNotEmpty()) {
      val boldFont = ResourcesCompat.getFont(context, boldFontResId)
      for (part in boldParts) {
        setSpan(FontSpan(boldFont), part.first, part.second, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
      }
    }

    if (italicParts.isNotEmpty()) {
      val italicFont = ResourcesCompat.getFont(context, italicFontResId)
      for (part in italicParts) {
        setSpan(FontSpan(italicFont), part.first, part.second, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
      }
    }
  }
  return this
}