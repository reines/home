package com.furnaghan.home.component.calendar.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.api.client.util.SecurityUtils;
import com.google.common.io.Files;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

public class GoogleCalendarConfiguration implements Configuration {

    @NotEmpty
    @JsonProperty
    private String email;

    @NotNull
    @JsonProperty
    private File keyStore;

    @NotNull
    @JsonProperty
    private String storePass = "notasecret";

    @NotNull
    @JsonProperty
    private String keyAlias = "privatekey";

    @NotNull
    @JsonProperty
    private String keyPass = "notasecret";

    @NotEmpty
    @JsonProperty
    private String calendarId;

    @NotNull
    @JsonProperty
    private Duration pollInterval = Duration.minutes(1);

    public String getEmail() {
        return email;
    }

    public PrivateKey getPrivateKey() throws GeneralSecurityException, IOException {
        try (final InputStream in = Files.asByteSource(keyStore).openStream()) {
            return SecurityUtils.loadPrivateKeyFromKeyStore(
                    SecurityUtils.getPkcs12KeyStore(), in,
                    storePass, keyAlias, keyPass
            );
        }
    }

    public String getCalendarId() {
        return calendarId;
    }

    public Duration getPollInterval() {
        return pollInterval;
    }
}
