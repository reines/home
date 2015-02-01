package com.furnaghan.home.component.email.imap.auth;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class PasswordAuthenticator extends Authenticator {

    private final String username;
    private final String password;

    public PasswordAuthenticator(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
