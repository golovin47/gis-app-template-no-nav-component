package com.gis.utils.presentation.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import io.reactivex.subjects.BehaviorSubject

class BluetoothAndNfcBroadcastReceiver : BroadcastReceiver() {

    private val bluetoothAndNfcStatePublisher = BehaviorSubject.create<BluetoothOrNfcState>()

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, Integer.MIN_VALUE)

            when (state) {
                BluetoothAdapter.STATE_OFF -> {
                    if (hasNfc(context) && !nfcEnabled(context) || !hasNfc(context))
                        bluetoothAndNfcStatePublisher.onNext(BluetoothOrNfcState.BLUETOOTH_AND_NFC_DISABLED)
                }

                BluetoothAdapter.STATE_ON -> {
                    bluetoothAndNfcStatePublisher.onNext(BluetoothOrNfcState.BLUETOOTH_OR_NFC_ENABLED)
                }
            }
        }

        if (action == NfcAdapter.ACTION_ADAPTER_STATE_CHANGED) {
            val state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE, Integer.MIN_VALUE)

            when (state) {
                NfcAdapter.STATE_OFF -> {
                    if (!bluetoothEnabled())
                        bluetoothAndNfcStatePublisher.onNext(BluetoothOrNfcState.BLUETOOTH_AND_NFC_DISABLED)
                }

                NfcAdapter.STATE_ON -> {
                    bluetoothAndNfcStatePublisher.onNext(BluetoothOrNfcState.BLUETOOTH_OR_NFC_ENABLED)
                }
            }
        }
    }

    fun observeBluetoothOrNfcState() = bluetoothAndNfcStatePublisher

    @SuppressLint("MissingPermission")
    private fun bluetoothEnabled() = BluetoothAdapter.getDefaultAdapter()?.isEnabled ?: false

    private fun hasNfc(context: Context) =
        (context.getSystemService(Context.NFC_SERVICE) as NfcManager).defaultAdapter != null

    private fun nfcEnabled(context: Context) =
        (context.getSystemService(Context.NFC_SERVICE) as NfcManager).defaultAdapter?.isEnabled ?: false

    enum class BluetoothOrNfcState {
        BLUETOOTH_OR_NFC_ENABLED,
        BLUETOOTH_AND_NFC_DISABLED
    }

}