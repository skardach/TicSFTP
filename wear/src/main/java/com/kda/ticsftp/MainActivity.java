package com.kda.ticsftp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener,
        WatchViewStub.OnLayoutInflatedListener {

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Intent intent = new Intent(this, SFTPServer.class);
        if (isChecked) {
            this.startService(intent);
        } else {
            this.stopService(intent);
        }
    }

    private ToggleButton mEnableServer;


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
        mEnableServer.setOnCheckedChangeListener(this);
    }
}
