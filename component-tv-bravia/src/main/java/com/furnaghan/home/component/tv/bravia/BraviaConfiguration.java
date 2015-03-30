package com.furnaghan.home.component.tv.bravia;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class BraviaConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private URI root;

    @NotEmpty
    @JsonProperty
    private String macAddress;

    public URI getUrl() {
        return root;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
