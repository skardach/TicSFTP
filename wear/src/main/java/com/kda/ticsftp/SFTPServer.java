package com.kda.ticsftp;

import android.util.Log;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.sftp.SftpSubsystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kardasan on 04/11/16.
 */

final class SFTPServer {
    public enum EEnableStatus {
        OK,
        IOException
    }
    public enum EDisableStatus {
        OK,
        InterruptedException
    }

    public SFTPServer() {
        passwordAuth.setUser("test");
        passwordAuth.setPassword("test");
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
    private final SimplePasswordAuthenticator passwordAuth = new SimplePasswordAuthenticator();
    private final ConfirmationActivityPublicKeyAuthenticator publicKeyAuth =
            new ConfirmationActivityPublicKeyAuthenticator();
    private final SimpleFileSystemFactory fileSystemFactory = new SimpleFileSystemFactory();
    private final SimpleGeneratorHostKeyProvider keyProvider =
            new SimpleGeneratorHostKeyProvider(serverKey, serverKeyType);

    // Server object
    private final SshServer sshd = SshServer.setUpDefaultServer();
    // Other stuff
    private static final String TAG="SFTPServer";
}
