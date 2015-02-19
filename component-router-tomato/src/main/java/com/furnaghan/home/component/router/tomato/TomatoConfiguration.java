package com.furnaghan.home.component.router.tomato;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class TomatoConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private final URI root;

    @NotEmpty
    @JsonProperty
    private final String username;

    @NotEmpty
    @JsonProperty
    private final String password;

    @NotNull
    @JsonProperty
    private final Duration pollInterval;

    @JsonCreator
    public TomatoConfiguration(
            @JsonProperty("root") final URI root,
            @JsonProperty("username") final String username,
            @JsonProperty("password") final String password,
            @JsonProperty("pollInterval") final Duration pollInterval) {
        this.root = root;
        this.username = username;
        this.password = password;
        this.pollInterval = pollInterval;
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

    public Duration getPollInterval() {
        return pollInterval;
    }
}
