package com.furnaghan.home.component.email.imap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.net.HostAndPort;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class ImapConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private final HostAndPort imapAddress;

    @NotNull
    @JsonProperty
    private final HostAndPort smtpAddress;

    @NotEmpty
    @JsonProperty
    private final String username;

    @NotEmpty
    @JsonProperty
    private final String password;

    @NotEmpty
    @JsonProperty
    private final String emailAddress;

    @JsonCreator
    public ImapConfiguration(
            @JsonProperty("imapAddress") final HostAndPort imapAddress,
            @JsonProperty("smtpAddress") final HostAndPort smtpAddress,
            @JsonProperty("username") final String username,
            @JsonProperty("password") final String password,
            @JsonProperty("emailAddress") final String emailAddress) {
        this.imapAddress = imapAddress;
        this.smtpAddress = smtpAddress;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
    }

    public HostAndPort getImapAddress() {
        return imapAddress;
    }

    public HostAndPort getSmtpAddress() {
        return smtpAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
