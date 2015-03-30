package com.furnaghan.home.component.email.imap;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.email.EmailType;
import com.furnaghan.home.component.email.imap.auth.PasswordAuthenticator;
import com.furnaghan.home.component.email.imap.util.ImapIdleRunnable;
import com.furnaghan.home.component.email.imap.util.MessageParsingUtils;
import com.google.common.base.Throwables;
import com.sun.mail.imap.IMAPFolder;
import io.dropwizard.util.Duration;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Executors;

public class ImapComponent extends Component<EmailType.Listener> implements EmailType {

    private static final Duration IMAP_POLL_INTERVAL = Duration.minutes(1);
    private static final String IMAP_PROTOCOL = "imaps";
    private static final String INBOX_FOLDER = "Inbox";
    private static final boolean SMTP_TLS = true;

    private final String from;
    private final Session session;

    public ImapComponent(final ImapConfiguration configuration) {
        final Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", SMTP_TLS);
        properties.put("mail.smtp.host", configuration.getSmtpAddress().getHostText());
        properties.put("mail.smtp.port", configuration.getSmtpAddress().getPortOrDefault(ImapConfiguration.DEFAULT_SMTP_PORT));

        from = configuration.getEmailAddress();
        session = Session.getInstance(properties, new PasswordAuthenticator(configuration.getUsername(), configuration.getPassword()));

        try {
            final Store imap = session.getStore(IMAP_PROTOCOL);
            imap.connect(
                    configuration.getImapAddress().getHostText(),
                    configuration.getImapAddress().getPortOrDefault(ImapConfiguration.DEFAULT_IMAP_PORT),
                    configuration.getUsername(),
                    configuration.getPassword()
            );

            final IMAPFolder inbox = (IMAPFolder) imap.getFolder(INBOX_FOLDER);
            inbox.open(Folder.READ_ONLY);
            inbox.addMessageCountListener(new MessageCountAdapter() {
                @Override
                public void messagesAdded(final MessageCountEvent event) {
                    for (final Message message : event.getMessages()) {
                        try {
                            final String from = message.getFrom()[0].toString();
                            final String subject = message.getSubject();
                            final String body = MessageParsingUtils.getText(message);

                            trigger((listener) -> listener.emailReceived(from, subject, body));
                        } catch (MessagingException | IOException e) {
                            throw Throwables.propagate(e);
                        }
                    }
                }
            });

            Executors.newSingleThreadExecutor().execute(new ImapIdleRunnable(inbox, IMAP_POLL_INTERVAL));
        } catch (MessagingException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void sendEmail(final String to, final String subject, final String body) {
        try {
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
        } catch (MessagingException e) {
            throw Throwables.propagate(e);
        }
    }
}
