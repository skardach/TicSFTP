package com.kda.ticsftp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.sftp.SftpSubsystem;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kardasan on 04/11/16.
 */

public final class SFTPServer extends Service {
    public static final String REQUEST_NAME = "com.kda.ticsftp.SFTPServer.REQ";
    public static final int START_SERVER = 0;
    public static final int STOP_SERVER = 1;
    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        SFTPServer getService() {
            return SFTPServer.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    protected enum EEnableStatus {
        OK,
        IOException
    }
    protected enum EDisableStatus {
        OK,
        InterruptedException
    }

    @Override
    public void onCreate() {
        super.onCreate();
        passwordAuth = new SimplePasswordAuthenticator();
        passwordAuth.setUser("test");
        passwordAuth.setPassword("test");
        publicKeyAuth = new ConfirmationActivityPublicKeyAuthenticator(this);
        fileSystemFactory = new SimpleFileSystemFactory();
        keyProvider = new SimpleGeneratorHostKeyProvider(serverKey, serverKeyType);
        sshd = SshServer.setUpDefaultServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EEnableStatus res = this.start();
        if (res != SFTPServer.EEnableStatus.OK) {
            Log.e(TAG, "Unable to start SFTP server: " + res.toString());
            return Service.START_NOT_STICKY;
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        this.stop();
    }

    public boolean showAcceptDialog(String user, PublicKey key) {
        return false;
    }

    public EEnableStatus start() {
        try {
            List<NamedFactory<Command>> subsystems = new ArrayList<>();
            // Set listen address and port
            sshd.setHost("0.0.0.0");
            sshd.setPort(port);
            // Set accepted key types (default RSA).
            sshd.setKeyPairProvider(keyProvider);
            // Set The
            sshd.setPublickeyAuthenticator(publicKeyAuth);
            sshd.setPasswordAuthenticator(passwordAuth);
            // Enable SFTP support
            subsystems.add(new SftpSubsystem.Factory());
            sshd.setSubsystemFactories(subsystems);
            // Setup root directory for SFTP
            fileSystemFactory.setRoot(sftpRoot);
            sshd.setFileSystemFactory(fileSystemFactory);
            sshd.start();
        } catch (IOException e) {
            Log.e(TAG, "Cannot start server: " + e.getMessage());
            return EEnableStatus.IOException;
        }
        return EEnableStatus.OK;
    }

    public EDisableStatus stop() {
        try {
            sshd.stop();
        } catch (InterruptedException e) {
            Log.e(TAG, "Cannot stop server: " + e.getMessage());
            return EDisableStatus.InterruptedException;
        }
        return EDisableStatus.OK;
    }

    // Configuration variables - move later on to settings.
    private int port = 9988;
    private String serverRoot = "/sdcard/sftp";
    private String serverKey = "/sdcard/sftp/key.srv";
    private String serverKeyType = "RSA";
    private String sftpRoot = serverRoot + "/root";

    // Server components
    private SimplePasswordAuthenticator passwordAuth;
    private ConfirmationActivityPublicKeyAuthenticator publicKeyAuth;
    private SimpleFileSystemFactory fileSystemFactory;
    private SimpleGeneratorHostKeyProvider keyProvider;
    // Server object
    private SshServer sshd;

    // Other stuff
    private static final String TAG="SFTPServer";
    // Android stuff
    private final IBinder binder = new LocalBinder();

}
