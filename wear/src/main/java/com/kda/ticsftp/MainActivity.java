package com.kda.ticsftp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class MainActivity extends Activity implements
        WatchViewStub.OnLayoutInflatedListener,
        WearableListView.ClickListener {
    // Constants
    private static final String TAG="TicSFTP_MainActivity";
    private static final String SRV_TGL_DSC_FMT = "Listening on %s:%d";
    private static final int LE_HOTSPOT_TAG = 0;
    private static final int LE_SERVER_TAG = 1;
    // State flags
    private boolean server_enabled = false;
    private boolean hotspot_enabled = false;
    // Main list
    private WearableListView mListView;
    // Main menu
    private ArrayList<MainListViewAdapter.ListElement> mElements;
    private MainListViewAdapter.ToggleListElement mHotspotToggle;
    private MainListViewAdapter.ToggleListElement mServerToggle;

    private IntentFilter mNetIntentFilter =
            new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    private BroadcastReceiver mNetBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mServerToggle.mDescription =
                    String.format(SRV_TGL_DSC_FMT, getWiFiAddrString(), Config.port);
            mListView.getAdapter().notifyDataSetChanged();
        }
    };

    String getWifiIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan") || intf.getName().contains("ap")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                            .hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()
                                && (inetAddress.getAddress().length == 4)) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }

    private String getWiFiAddrString() {
        String ip = getWifiIpAddress();
        if (ip == null)
            ip = "(none)";
        return ip;
    }

    private void handleServerChange(boolean enable) {
        SharedPreferences.Editor editor =
                this.getSharedPreferences(Config.TAG_SHP, Context.MODE_PRIVATE).edit();
        Intent intent = new Intent(this, SFTPServer.class);
        if (enable) {
            server_enabled = true;
            this.startService(intent);
        } else {
            server_enabled = false;
            this.stopService(intent);
        }
        editor.putBoolean(Config.SHP_SRV_ENABLED, server_enabled);
        editor.apply();
    }

    private void handleHotspotChange(boolean enable) {
        HotSpotManager.configApState(this, enable);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mElements = new ArrayList<>();
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(this);
    }

    @Override
    public void onLayoutInflated(WatchViewStub watchViewStub) {
        fillMainMenu(mElements);
        // Get the list component from the layout of the activity
        mListView = (WearableListView) findViewById(R.id.lv_main);
        // Assign an adapter to the list
        mListView.setAdapter(new MainListViewAdapter(this, mElements));
        // Set a click listener
        mListView.setClickListener(this);
        mNetIntentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        this.registerReceiver(mNetBroadcastReceiver, mNetIntentFilter);
    }

    private void
    fillMainMenu(ArrayList<MainListViewAdapter.ListElement> menuElements) {
        mHotspotToggle = new MainListViewAdapter.ToggleListElement(
                LE_HOTSPOT_TAG,
                getString(R.string.enable_hotspot),
                String.format("AP: %s, pass: %s", Config.hotspotName,
                        Config.hotspotPassword),
                hotspot_enabled);
        mServerToggle = new MainListViewAdapter.ToggleListElement(
                LE_SERVER_TAG,
                getString(R.string.enable_server),
                String.format(SRV_TGL_DSC_FMT, getWiFiAddrString(),
                        Config.port),
                server_enabled);
        menuElements.add(mServerToggle);
        menuElements.add(mHotspotToggle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Config.SHP_SRV_ENABLED, server_enabled);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        server_enabled = savedInstanceState.getBoolean(Config.SHP_SRV_ENABLED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        server_enabled =
            this.getSharedPreferences(Config.TAG_SHP,
                    Context.MODE_PRIVATE).getBoolean(
                        Config.SHP_SRV_ENABLED,
                        server_enabled);
        hotspot_enabled = HotSpotManager.isApOn(this);
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        Integer tag = (Integer) viewHolder.itemView.getTag();
        switch (tag) {
            case LE_HOTSPOT_TAG:
                mHotspotToggle.handleClick(this, viewHolder);
                handleHotspotChange(mHotspotToggle.isToggled());
                break;
            case LE_SERVER_TAG:
                mServerToggle.handleClick(this, viewHolder);
                handleServerChange(mServerToggle.isToggled());
                break;
        }
    }

    @Override
    public void onTopEmptyRegionClick() {
    }
}
