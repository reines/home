package com.furnaghan.home.component.torrent.deluge.client.api.commands;

import com.furnaghan.home.component.torrent.deluge.client.api.Command;
import com.google.common.collect.ImmutableList;

import java.net.URI;

public class DownloadTorrentCommand extends Command {

    public DownloadTorrentCommand(URI path) {
        super("web.download_torrent_from_url", ImmutableList.of(path.toString()));
    }
}
