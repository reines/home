package com.furnaghan.home.component.tv.bravia;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class BraviaConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private final URI root;

    @NotEmpty
    @JsonProperty
    private final String macAddress;

    @JsonCreator
    public BraviaConfiguration(
            @JsonProperty("root") URI root,
            @JsonProperty("macAddress") String macAddress) {
        this.root = root;
        this.macAddress = macAddress;
    }

    public URI getUrl() {
        return root;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
