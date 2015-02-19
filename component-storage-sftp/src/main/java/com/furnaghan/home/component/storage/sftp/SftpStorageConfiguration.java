package com.furnaghan.home.component.storage.sftp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.net.HostAndPort;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class SftpStorageConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private final HostAndPort address;

    @NotEmpty
    @JsonProperty
    private final String username;

    @NotEmpty
    @JsonProperty
    private final String password;

    @JsonCreator
    public SftpStorageConfiguration(
            @JsonProperty("address") final HostAndPort address,
            @JsonProperty("username") final String username,
            @JsonProperty("password") final String password) {
        this.address = address;
        this.username = username;
        this.password = password;
    }

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
