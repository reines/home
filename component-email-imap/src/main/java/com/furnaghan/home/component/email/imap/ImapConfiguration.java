package com.furnaghan.home.component.email.imap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.net.HostAndPort;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class ImapConfiguration implements Configuration {

    public static final int DEFAULT_IMAP_PORT = 143;
    public static final int DEFAULT_SMTP_PORT = 25;

    @NotNull
    @JsonProperty
    private HostAndPort imapAddress;

    @NotNull
    @JsonProperty
    private HostAndPort smtpAddress;

    @NotEmpty
    @JsonProperty
    private String username;

    @NotEmpty
    @JsonProperty
    private String password;

    @NotEmpty
    @JsonProperty
    private String emailAddress;

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
