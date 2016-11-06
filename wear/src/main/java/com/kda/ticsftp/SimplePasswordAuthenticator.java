package com.kda.ticsftp;

import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.session.ServerSession;

/**
 * Created by kardasan on 05/11/16.
 */

final class SimplePasswordAuthenticator implements PasswordAuthenticator {

    private String user;
    private String password;

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean authenticate(String user, String password,
                                ServerSession session) {
        return user.equals(this.user) && password.equals(this.password);
    }
}
