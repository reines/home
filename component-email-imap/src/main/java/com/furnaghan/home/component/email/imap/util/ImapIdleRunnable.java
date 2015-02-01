package com.furnaghan.home.component.email.imap.util;

import com.sun.mail.imap.IMAPFolder;
import io.dropwizard.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.MessagingException;

public class ImapIdleRunnable implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ImapIdleRunnable.class);

    private final IMAPFolder folder;
    private final Duration pollInterval;

    public ImapIdleRunnable(final IMAPFolder folder, final Duration pollInterval) {
        this.folder = folder;
        this.pollInterval = pollInterval;
    }

    @Override
    public void run() {
        boolean supportsIdle = true;
        try {
            folder.idle();
        } catch (MessagingException e) {
            LOG.warn("Server doesn't support IMAP IDLE, falling back to polling every {}", pollInterval);
            supportsIdle = false;
        }

        while (true) {
            try {
                if (supportsIdle) {
                    folder.idle();
                } else {
                    folder.getMessageCount();
                    Thread.sleep(pollInterval.toMilliseconds());
                }
            } catch (Exception e) {
                LOG.warn("Error checking for inbox messages", e);
            }
        }
    }
}
