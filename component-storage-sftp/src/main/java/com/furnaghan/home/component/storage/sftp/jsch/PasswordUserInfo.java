package com.furnaghan.home.component.storage.sftp.jsch;

import com.jcraft.jsch.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordUserInfo implements UserInfo {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordUserInfo.class);

    private final String password;

    public PasswordUserInfo(final String password) {
        this.password = password;
    }

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean promptPassword(final String message) {
        return true;
    }

    @Override
    public boolean promptPassphrase(final String message) {
        return false;
    }

    @Override
    public boolean promptYesNo(final String message) {
        return false;
    }

    @Override
    public void showMessage(final String message) {
        LOG.debug(message);
    }
}
