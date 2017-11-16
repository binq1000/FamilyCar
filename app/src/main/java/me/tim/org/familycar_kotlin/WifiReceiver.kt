package me.tim.org.familycar_kotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.widget.Toast

/**
 * Created by Nekkyou on 8-11-2017.
 */
class WifiReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            val info: NetworkInfo = intent?.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)!!

            //Wifi is connected.
            Toast.makeText(context, "Wifi connected", Toast.LENGTH_SHORT).show()
        }
    }
}