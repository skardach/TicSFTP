package com.kda.ticsftp;

import android.util.Log;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

class HotSpotManager {
    private static final String TAG="HotSpotManager";
    private boolean was_wifi_on = false;
    private String hotspotName = Config.hotspotName;
    private String hotspotPassword = Config.hotspotPassword;


    public String getHotspotName() {
        return hotspotName;
    }

    public String getHotspotPassword() {
        return hotspotPassword;
    }

    HotSpotManager(String hotspotName, String hotspotPassword) {
        if (hotspotName != null)
            this.hotspotName = hotspotName;
        if (hotspotPassword != null)
            this.hotspotPassword = hotspotPassword;
    }
    //check whether wifi hotspot on or off
    boolean isApOn(Context context) {
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        try {
            Method method = wifimanager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(wifimanager);
        }
        catch (Throwable t) {
            Log.e(TAG, Log.getStackTraceString(t));
        }
        return false;
    }

    // toggle wifi hotspot on or off
    synchronized boolean configApState(Context context, boolean enable) {
        if (enable == isApOn(context))
            return true;
        WifiManager wifimanager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiConfiguration wificonfiguration = null;
        try {
            if (enable) {
                if (wifimanager.isWifiEnabled()) {
                    wifimanager.setWifiEnabled(false);
                    was_wifi_on = true;
                }
                wificonfiguration = new WifiConfiguration();
                wificonfiguration.hiddenSSID = false;
                wificonfiguration.SSID = Config.hotspotName;
                wificonfiguration.preSharedKey = Config.hotspotPassword;
                wificonfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wificonfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                wificonfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                wificonfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                wificonfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wificonfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wificonfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wificonfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }
            Method method = wifimanager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.invoke(wifimanager, wificonfiguration, enable);
            if (!enable && was_wifi_on) {
                wifimanager.setWifiEnabled(true);
            }
            return true;
        }
        catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return false;
    }
}
