package com.furnaghan.home.component.calendar.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.util.SecurityUtils;
import com.google.common.base.Optional;
import com.google.common.io.Files;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

public class GoogleCalendarConfiguration {

    private static final String DEFAULT_STORE_PASS = "notasecret";
    private static final String DEFAULT_KEY_ALIAS = "privatekey";
    private static final String DEFAULT_KEY_PASS = "notasecret";

    @NotEmpty
    @JsonProperty
    private final String email;

    @NotNull
    @JsonProperty
    private final File keyStore;

    @NotNull
    @JsonProperty
    private final Optional<String> storePass;

    @NotNull
    @JsonProperty
    private final Optional<String> keyAlias;

    @NotNull
    @JsonProperty
    private final Optional<String> keyPass;

    @NotEmpty
    @JsonProperty
    private final String calendarId;

    @NotNull
    @JsonProperty
    private final Duration pollInterval;

    public GoogleCalendarConfiguration(
            @JsonProperty("email") final String email,
            @JsonProperty("keyStore") final File keyStore,
            @JsonProperty("storePass") final Optional<String> storePass,
            @JsonProperty("keyAlias") final Optional<String> keyAlias,
            @JsonProperty("keyPass") final Optional<String> keyPass,
            @JsonProperty("calendarId") final String calendarId,
            @JsonProperty("pollInterval") final Duration pollInterval) {
        this.email = email;
        this.keyStore = keyStore;
        this.storePass = storePass;
        this.keyAlias = keyAlias;
        this.keyPass = keyPass;
        this.calendarId = calendarId;
        this.pollInterval = pollInterval;
    }

    public String getEmail() {
        return email;
    }

    public PrivateKey getPrivateKey() throws GeneralSecurityException, IOException {
        try (final InputStream in = Files.asByteSource(keyStore).openStream()) {
            return SecurityUtils.loadPrivateKeyFromKeyStore(
                    SecurityUtils.getPkcs12KeyStore(),
                    in,
                    storePass.or(DEFAULT_STORE_PASS),
                    keyAlias.or(DEFAULT_KEY_ALIAS),
                    keyPass.or(DEFAULT_KEY_PASS)
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
