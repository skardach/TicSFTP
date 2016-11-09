package com.kda.ticsftp;

import org.apache.sshd.server.SshFile;
import org.apache.sshd.server.filesystem.NativeFileSystemView;

final class SimpleFileSystemView extends NativeFileSystemView {

    public SimpleFileSystemView(String userName, String root) {
        super(userName, false);
        if (root == null || root.equals("/")) {
            throw new IllegalArgumentException("Root folder cannot be empty or null");
        }
        this.root = root;
    }

    @Override
    public SshFile getFile(String file) {
        return getFile(root, file);
    }

    private String root;
}
