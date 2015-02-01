package com.furnaghan.home.component.modem.sky.client;

import com.furnaghan.home.component.modem.model.LineStats;
import com.furnaghan.util.Speed;
import com.google.common.base.Throwables;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkyClient {
    private static final String SYSTEM_PATH = "sky_system.html";

    private static final Pattern CONNECTION_SPEED_PATTERN = Pattern.compile(
            "<tr>\\s*<th>Connection Speed</th>\\s*<td>(.*)</td>\\s*<td>(.*)</td>\\s*</tr>", Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern DSL_STATS_PATTERN = Pattern.compile(
            "var dslSts = '(\\w+)_([\\d\\.]+)_([\\d\\.]+)_([\\d\\.]+)_([\\d\\.]+)_([\\d\\.]+)_([\\d\\.]+)';");

    private static String unescapeHtml(String str) {
        return StringEscapeUtils.unescapeHtml(str).replace(' ', ' ');
    }

    private final Client client;
    private final URI root;

    public SkyClient(final Client client, final URI root, final String username, final String password) {
        client.addFilter(new HTTPBasicAuthFilter(username, password));

        this.client = client;
        this.root = root;
    }

    public LineStats getLineStats() {
        final String html = client.resource(root)
                .path(SYSTEM_PATH)
                .get(String.class);

        final Matcher connectionSpeedMatcher = CONNECTION_SPEED_PATTERN.matcher(html);
        final Matcher dslStatsMatcher = DSL_STATS_PATTERN.matcher(html);
        if (!connectionSpeedMatcher.find() || !dslStatsMatcher.find()) {
            throw Throwables.propagate(new IOException("Unable to find connection speed or dsl stats."));
        }

        return new LineStats(
                Speed.parse(unescapeHtml(connectionSpeedMatcher.group(1))),
                Double.parseDouble(dslStatsMatcher.group(4)),
                Double.parseDouble(dslStatsMatcher.group(6)),
                Speed.parse(unescapeHtml(connectionSpeedMatcher.group(2))),
                Double.parseDouble(dslStatsMatcher.group(5)),
                Double.parseDouble(dslStatsMatcher.group(7))
        );
    }
}
