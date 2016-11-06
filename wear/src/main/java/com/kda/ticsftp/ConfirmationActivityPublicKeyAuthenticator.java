package com.kda.ticsftp;

import org.apache.sshd.server.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kardasan on 05/11/16.
 */

final class ConfirmationActivityPublicKeyAuthenticator implements PublickeyAuthenticator {

        private List<PublicKey> keys = new ArrayList<PublicKey>();

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
            return false;
        }
}
