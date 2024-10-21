package jaredDeFour.example.comp3606a1.wifidirect

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pGroup

interface WifiDirectInterface {
    fun onWiFiDirectStateChanged(isEnabled:Boolean)
    fun onGroupStatusChanged(groupInfo: WifiP2pGroup?)
    fun onDeviceStatusChanged(thisDevice: WifiP2pDevice)
    fun setNetworkDetails(ssid: String?, password: String?)
}