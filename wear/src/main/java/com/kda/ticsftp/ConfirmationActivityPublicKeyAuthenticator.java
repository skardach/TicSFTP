package com.kda.ticsftp;

import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

final class ConfirmationActivityPublicKeyAuthenticator implements PublickeyAuthenticator {

    public ConfirmationActivityPublicKeyAuthenticator(SFTPServer parent) {
        this.parent = parent;
    }

    public void addKey(PublicKey key) {
        keys.add(key);
    }

    @Override
    public boolean authenticate(String user, PublicKey key, ServerSession session) {
        for (PublicKey k : keys) {
            if (key.equals(k)) {
                return true;
            }
        }
        boolean acceptKey = parent.showAcceptDialog(user, key);
        if (acceptKey)
            addKey(key);
        return acceptKey;
    }

    private List<PublicKey> keys = new ArrayList<PublicKey>();
    private SFTPServer parent;
}
