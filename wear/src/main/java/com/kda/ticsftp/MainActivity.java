package com.kda.ticsftp;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

    private class EnableServerListener implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                SFTPServer.EEnableStatus res = mSftpServer.start();
                if (res != SFTPServer.EEnableStatus.OK) {
                    Log.e(TAG, "Unable to start SFTP server: " + res.toString());
                }
            } else {
                SFTPServer.EDisableStatus res = mSftpServer.stop();
                if (res != SFTPServer.EDisableStatus.OK) {
                    Log.e(TAG, "Unable to stop SFTP server: " + res.toString());
                }
            }
        }
    }
    private static final String TAG = "MainActivity";
    private ToggleButton mEnableServer;
    private SFTPServer mSftpServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSftpServer = new SFTPServer();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mEnableServer = (ToggleButton) stub.findViewById(R.id.tb_enableServer);
                mEnableServer.setOnCheckedChangeListener(new EnableServerListener());
            }
        });
    }
}
