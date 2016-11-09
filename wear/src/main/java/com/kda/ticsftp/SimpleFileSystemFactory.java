package com.kda.ticsftp;

import org.apache.sshd.common.Session;
import org.apache.sshd.server.FileSystemFactory;
import org.apache.sshd.server.FileSystemView;

import java.io.IOException;

final class SimpleFileSystemFactory implements FileSystemFactory {

    @Override
    public FileSystemView createFileSystemView(Session session) throws IOException {
        return new SimpleFileSystemView(session.getUsername(), root);
    }

    public void setRoot(String newRoot) {
        root = newRoot;
    }

    private String root = "";
}
