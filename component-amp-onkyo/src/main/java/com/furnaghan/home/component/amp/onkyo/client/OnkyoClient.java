package com.furnaghan.home.component.amp.onkyo.client;

import com.google.common.base.Throwables;
import com.google.common.net.HostAndPort;
import de.csmp.jeiscp.EiscpConnector;

import java.io.IOException;

// https://github.com/kliron/OnkyoEiscp/blob/master/OnkyoEiscp.rb
// https://github.com/compbrain/Onkyo-TX-NR708-Control/blob/master/eiscp/commands.py
public class OnkyoClient {

    public static OnkyoClient autodiscover() {
        try {
            return new OnkyoClient(EiscpConnector.autodiscover());
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private final EiscpConnector connector;

    public OnkyoClient(final HostAndPort address) {
        try {
            connector = new EiscpConnector(address.getHostText(), address.getPort());
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private OnkyoClient(final EiscpConnector connector) {
        this.connector = connector;
    }

    public void addListener(final CommandListener listener) {
        connector.addListener((input) -> {
            final String command = input.substring(0, 3);
            final int value = Integer.parseInt(input.substring(3), 16);
            listener.onCommand(command, value);
        });
    }

    public void sendCommand(final String command) {
        try {
            connector.sendIscpCommand(command);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
