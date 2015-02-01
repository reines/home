package com.furnaghan.home.component.torrent.deluge.client.api.commands;

import com.furnaghan.home.component.torrent.deluge.client.api.Command;
import com.google.common.collect.ImmutableList;

public class LoginCommand extends Command {

    public LoginCommand(String password) {
        super("auth.login", ImmutableList.of(password));
    }
}
