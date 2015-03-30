package com.furnaghan.home.component.storage.sftp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.net.HostAndPort;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class SftpStorageConfiguration implements Configuration {

    public static final int DEFAULT_SSH_PORT = 22;

    @NotNull
    @JsonProperty
    private HostAndPort address;

    @NotEmpty
    @JsonProperty
    private String username;

    @NotEmpty
    @JsonProperty
    private String password;

    public HostAndPort getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
