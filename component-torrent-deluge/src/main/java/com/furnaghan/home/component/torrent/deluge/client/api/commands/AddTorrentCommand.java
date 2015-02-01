package com.furnaghan.home.component.torrent.deluge.client.api.commands;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.torrent.deluge.client.api.Command;
import com.furnaghan.home.component.torrent.deluge.client.api.Options;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import io.dropwizard.jackson.JsonSnakeCase;

import java.util.Collection;

public class AddTorrentCommand extends Command {

    @JsonSnakeCase
    private static class PathObject {

        @JsonProperty
        private final String path;

        @JsonProperty
        private final Options options;

        @JsonCreator
        private PathObject(
                @JsonProperty("path") String path,
                @JsonProperty("options") Options options) {
            this.path = path;
            this.options = options;
        }
    }

    private static class PathToObjectFunction implements Function<String, Object> {
        @Override
        public Object apply(String input) {
            return new PathObject(input, Options.DEFAULT_OPTIONS);
        }
    }

    public AddTorrentCommand(Collection<String> files) {
        super("web.add_torrents", ImmutableList.of(Collections2.transform(files, new PathToObjectFunction())));
    }
}
