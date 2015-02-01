package com.furnaghan.home.component.torrent.deluge.client.api.commands;

import com.furnaghan.home.component.torrent.deluge.client.api.Command;
import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UpdateUiCommand extends Command {

    private static final List<String> FIELDS = Collections.emptyList(); // all fields
    private static final Map<String, Object> OPTIONS = Collections.emptyMap();

    public UpdateUiCommand() {
        super("web.update_ui", ImmutableList.of(FIELDS, OPTIONS));
    }
}
