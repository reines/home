package com.furnaghan.home.component.modem.sky;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class SkyModemConfiguration extends JerseyClientConfiguration implements Configuration {
    @NotNull
    @JsonProperty
    private final URI root;

    @NotEmpty
    @JsonProperty
    private final String username;

    @NotEmpty
    @JsonProperty
    private final String password;

    @JsonCreator
    public SkyModemConfiguration(
            @JsonProperty("root") URI root,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password) {
        this.root = root;
        this.username = username;
        this.password = password;
    }

    public URI getRoot() {
        return root;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
