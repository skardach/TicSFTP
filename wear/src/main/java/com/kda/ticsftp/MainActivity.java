package com.kda.ticsftp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener,
        WatchViewStub.OnLayoutInflatedListener {
    private final String STATE_SRV_ENABLED = "com.kda.ticftp.SrvEnabled";
    private boolean server_enabled = false;
    private ToggleButton mEnableServer;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences.Editor editor =
                this.getSharedPreferences("com.kda.ticftp", Context.MODE_PRIVATE).edit();
        Intent intent = new Intent(this, SFTPServer.class);
        if (isChecked) {
            server_enabled = true;
            this.startService(intent);
        } else {
            server_enabled = false;
            this.stopService(intent);
        }
        editor.putBoolean(STATE_SRV_ENABLED, server_enabled);
        editor.commit();
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
        mEnableServer = (ToggleButton) findViewById(R.id.tb_enableServer);
        mEnableServer.setChecked(server_enabled);
        mEnableServer.setOnCheckedChangeListener(this);
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
    }
}
