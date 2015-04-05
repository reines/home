package com.furnaghan.home.component.heating.evohome;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class EvohomeConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotEmpty
    @JsonProperty
    private String username;

    @NotEmpty
    @JsonProperty
    private String password;

    @NotNull
    @JsonProperty
    private Duration pollInterval = Duration.minutes(1);

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
