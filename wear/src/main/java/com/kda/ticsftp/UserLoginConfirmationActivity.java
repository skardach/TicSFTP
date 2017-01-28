package com.kda.ticsftp;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DelayedConfirmationView;
import android.support.wearable.view.WatchViewStub;
import android.view.View;

public class UserLoginConfirmationActivity extends Activity implements DelayedConfirmationView.DelayedConfirmationListener {
    private DelayedConfirmationView mConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_confirmation);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mConfirmation = (DelayedConfirmationView) stub.findViewById(R.id.login_confirm);
                mConfirmation.setListener(UserLoginConfirmationActivity.this);
                mConfirmation.setTotalTimeMs(Config.pubKeyTimeout);
                mConfirmation.start();
            }
        });
    }

    @Override
    public void onTimerFinished(View view) {
        synchronized (SFTPServer.AUTH_LOCK) {
            SFTPServer.AUTH_LOCK.notify();
            finish();
        }
    }

    @Override
    public void onTimerSelected(View view) {
        synchronized (SFTPServer.AUTH_LOCK) {
            SFTPServer.AUTH_OK = true;
            SFTPServer.AUTH_LOCK.notify();
            finish();
        }
    }
}
