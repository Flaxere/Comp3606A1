package jaredDeFour.example.comp3606a1.wifidirect

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pGroup
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.WifiP2pManager.ActionListener
import android.os.Build
import android.util.Log
import android.widget.LinearLayout
import jaredDeFour.example.comp3606a1.R

class WifiDirectManager(private val manager: WifiP2pManager,
                        private val channel: WifiP2pManager.Channel,
                        private val iFaceImpl: WifiDirectInterface):BroadcastReceiver() {
    var groupInfo: WifiP2pGroup? = null

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                val isWifiP2pEnabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED
                iFaceImpl.onWiFiDirectStateChanged(isWifiP2pEnabled)
                Log.e("WFDManager","The WiFi direct adapter state has changed to $isWifiP2pEnabled")
            }

            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                val wifiP2pInfo = when{
                    Build.VERSION.SDK_INT >= 33 -> intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO, WifiP2pInfo::class.java)!!
                    else -> @Suppress("DEPRECATION") intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO)!!
                }
                val tmpGroupInfo = when{
                    !(wifiP2pInfo.groupFormed)->null
                    Build.VERSION.SDK_INT >= 33 -> intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP, WifiP2pGroup::class.java)!!
                    else -> @Suppress("DEPRECATION") intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_GROUP)!!
                }
                if (groupInfo != tmpGroupInfo){
                    groupInfo = tmpGroupInfo
                    Log.e("WFDManager","The class has been started")
                    iFaceImpl.onGroupStatusChanged(groupInfo)
                    val ssid = groupInfo?.networkName
                    val pass = groupInfo?.passphrase
                    iFaceImpl.setNetworkDetails(ssid,pass)

                }


            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                val thisDevice = when{
                    Build.VERSION.SDK_INT >= 33 -> intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE, WifiP2pDevice::class.java)!!
                    else -> @Suppress("DEPRECATION") intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)!!
                }


                Log.e("WFDManager","The device status has changed")
                iFaceImpl.onDeviceStatusChanged(thisDevice)
            }
        }
    }


    @SuppressLint("MissingPermission")
    fun createGroup(){
        manager.createGroup(channel, object : ActionListener {
            override fun onSuccess() {
                Log.e("WFDManager","Successfully started the class")
            }

            override fun onFailure(reason: Int) {
                Log.e("WFDManager","An error occurred while trying to start class")
            }

        })
    }



    fun disconnect(){
        manager.removeGroup(channel, object : ActionListener {
            override fun onSuccess() {
                Log.e("WFDManager","Successfully ended class")
            }
            override fun onFailure(reason: Int) {
                Log.e("WFDManager","An error occurred while trying to disconnect from the group")
            }

        })
    }



}