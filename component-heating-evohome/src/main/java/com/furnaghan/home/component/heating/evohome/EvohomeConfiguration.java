package com.furnaghan.home.component.heating.evohome;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class EvohomeConfiguration extends JerseyClientConfiguration {

    @NotEmpty
    @JsonProperty
    private final String username;

    @NotEmpty
    @JsonProperty
    private final String password;

    @NotNull
    @JsonProperty
    private final Duration pollInterval;

    public EvohomeConfiguration(
            @JsonProperty("username") final String username,
            @JsonProperty("password") final String password,
            @JsonProperty("pollInterval") final Duration pollInterval) {
        this.username = username;
        this.password = password;
        this.pollInterval = pollInterval;
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
