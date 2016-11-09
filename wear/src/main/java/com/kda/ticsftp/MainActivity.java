package com.kda.ticsftp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener,
        WatchViewStub.OnLayoutInflatedListener {
    private final String SHARED_PREFS_TAG = "com.kda.ticftp";
    private final String STATE_SRV_ENABLED = "com.kda.ticftp.SrvEnabled";
    private boolean server_enabled = false;
    private boolean hotspot_enabled = false;
    private ToggleButton mEnableServer;
    private ToggleButton mEnableHotSpot;
    private TextView mEnableHotSpotLabel;

    private void handleServerChange(boolean enable) {
        SharedPreferences.Editor editor =
                this.getSharedPreferences(SHARED_PREFS_TAG, Context.MODE_PRIVATE).edit();
        Intent intent = new Intent(this, SFTPServer.class);
        if (enable) {
            server_enabled = true;
            this.startService(intent);
        } else {
            server_enabled = false;
            this.stopService(intent);
        }
        editor.putBoolean(STATE_SRV_ENABLED, server_enabled);
        editor.commit();
    }

    private void handleHotspotChange(boolean enable) {
        HotSpotManager.configApState(this, enable);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.equals(mEnableServer)) {
            handleServerChange(isChecked);
        } else if (buttonView.equals(mEnableHotSpot)) {
            handleHotspotChange(isChecked);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(this);
    }

    @Override
    public void onLayoutInflated(WatchViewStub watchViewStub) {
        mEnableServer = (ToggleButton) findViewById(R.id.tb_enable_server);
        mEnableServer.setChecked(server_enabled);
        mEnableServer.setOnCheckedChangeListener(this);
        mEnableHotSpot = (ToggleButton) findViewById(R.id.tb_enable_hotspot);
        mEnableHotSpot.setChecked(hotspot_enabled);
        mEnableHotSpot.setOnCheckedChangeListener(this);
        mEnableHotSpotLabel = (TextView) findViewById(R.id.tv_enable_hotspot);
        mEnableHotSpotLabel.setText(getResources().getString(R.string.enable_hotspot) +
                " (" + SFTPServer.getHotspotName() + ")");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SRV_ENABLED, server_enabled);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        server_enabled = savedInstanceState.getBoolean(STATE_SRV_ENABLED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        server_enabled =
            this.getSharedPreferences(
                "com.kda.ticftp",
                Context.MODE_PRIVATE).getBoolean(STATE_SRV_ENABLED, server_enabled);
        hotspot_enabled = HotSpotManager.isApOn(this);

    }
}
