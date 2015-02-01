package com.furnaghan.home.component.storage.sftp;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.storage.StorageType;
import com.furnaghan.home.component.storage.sftp.jsch.PasswordUserInfo;
import com.google.common.base.Throwables;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SftpStorageComponent extends Component<StorageType.Listener> implements StorageType {

    private static final String SFTP_PROTOCOL = "sftp";

    private final ChannelSftp sftp;

    public SftpStorageComponent(final SftpStorageConfiguration configuration) {
        try {
            final Session session = new JSch().getSession(
                    configuration.getUsername(),
                    configuration.getAddress().getHostText(),
                    configuration.getAddress().getPort()
            );
            session.setUserInfo(new PasswordUserInfo(configuration.getPassword()));
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            sftp = (ChannelSftp) session.openChannel(SFTP_PROTOCOL);
            sftp.connect();
        } catch (JSchException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public boolean exists(final String path) {
        try {
            sftp.stat(path);
            return true;
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                return false;
            }

            throw Throwables.propagate(e);
        }
    }

    @Override
    public void rename(final String source, final String destination) {
        try {
            sftp.rename(source, destination);
        } catch (SftpException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void delete(final String path) {
        try {
            sftp.rm(path);
        } catch (SftpException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void mkdir(final String path) {
        try {
            sftp.mkdir(path);
        } catch (SftpException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public ByteSource read(final String path) {
        return new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                try {
                    return sftp.get(path);
                } catch (SftpException e) {
                    throw Throwables.propagate(e);
                }
            }
        };
    }

    @Override
    public ByteSink write(final String path, final boolean append) {
        return new ByteSink() {
            @Override
            public OutputStream openStream() throws IOException {
                try {
                    return sftp.put(path, append ? ChannelSftp.APPEND : ChannelSftp.OVERWRITE);
                } catch (SftpException e) {
                    throw Throwables.propagate(e);
                }
            }
        };
    }
}
