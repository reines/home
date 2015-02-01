package com.furnaghan.home.component.email.imap.util;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public final class MessageParsingUtils {

    private static final String MIME_TYPE_TEXT_WILDCARD = "text/*";
    private static final String MIME_TYPE_MULTIPART_ALTERNATIVE = "multipart/alternative";
    private static final String MIME_TYPE_MULTIPART_WILDCARD = "multipart/*";

    public static String getText(final Part part) throws MessagingException, IOException {
        if (part.isMimeType(MIME_TYPE_TEXT_WILDCARD)) {
            return (String) part.getContent();
        }

        if (part.isMimeType(MIME_TYPE_MULTIPART_ALTERNATIVE)) {
            // prefer html text over plain text
            final Multipart mp = (Multipart) part.getContent();

            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                final Part bp = mp.getBodyPart(i);
                if (bp.isMimeType(MediaType.TEXT_PLAIN)) {
                    if (text == null) {
                        text = getText(bp);
                    }
                } else if (bp.isMimeType(MediaType.TEXT_HTML)) {
                    final String s = getText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }

            return text;
        }

        if (part.isMimeType(MIME_TYPE_MULTIPART_WILDCARD)) {
            final Multipart mp = (Multipart) part.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                final String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }

    private MessageParsingUtils() {
    }
}
