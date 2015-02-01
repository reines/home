package com.furnaghan.home.component.email;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

public interface EmailType extends ComponentType {
    public static interface Listener extends Component.Listener {
        void emailReceived(final String from, final String subject, final String body);
    }

    void sendEmail(final String to, final String subject, final String body);
}
