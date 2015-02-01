package com.furnaghan.home.component.router.tomato.client;

import com.furnaghan.home.component.router.model.Interface;
import com.furnaghan.home.component.router.model.MacAddress;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.representation.Form;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TomatoClient {

    private static final String HOME_PATH = "/";
    private static final String SHELL_PATH = "/shell.cgi";
    private static final String GET_CONNECTED_DEVICES_COMMAND = "wl assoclist";
    private static final String GET_NET_DEV_COMMAND = "cat /proc/net/dev";
    private static final Pattern HTTP_ID_PATTERN = Pattern.compile("http_id=([a-zA-Z0-9]+)'");
    private static final Pattern COMMAND_RESULT_PATTERN = Pattern.compile("cmdresult = '(.*?)';", Pattern.MULTILINE | Pattern.DOTALL);
    private static final Pattern NETDEV_TABLE_PATTERN = Pattern.compile("(\\w+):((?:\\s*\\d+){16})");
    private static final Splitter NETDEV_FIELD_SPLITTER = Splitter.onPattern("[\\s\\|]+").trimResults().omitEmptyStrings();

    private final Client client;
    private final URI root;
    private final String httpId;

    public TomatoClient(final Client client, final URI root, final String username, final String password) {
        client.addFilter(new HTTPBasicAuthFilter(username, password));

        this.client = client;
        this.root = root;

        httpId = fetchHttpId();
    }

    private String fetchHttpId() {
        final String html = client.resource(root)
                .path(HOME_PATH)
                .get(String.class);

        final Matcher httpIdMatcher = HTTP_ID_PATTERN.matcher(html);
        if (!httpIdMatcher.find()) {
            throw Throwables.propagate(new IOException("Unable to find http ID"));
        }

        return httpIdMatcher.group(1);
    }

    private String execute(String command) throws IOException {
        final Form payload = new Form();
        payload.add("action", "execute");
        payload.add("command", command);
        payload.add("_http_id", httpId);

        final String html = client.resource(root)
                .path(SHELL_PATH)
                .type(MediaType.TEXT_PLAIN)
                .post(String.class, payload);

        final Matcher commandResultMatch = COMMAND_RESULT_PATTERN.matcher(html);
        if (!commandResultMatch.find()) {
            throw new IOException(String.format("Unable to find command result in:\n%s", html));
        }

        return commandResultMatch.group(1);
    }

    public Set<MacAddress> getConnectedDevices() throws IOException {
        final String associations = execute(GET_CONNECTED_DEVICES_COMMAND);
        final Matcher macAddressMatcher = MacAddress.PATTERN.matcher(associations);

        final ImmutableSet.Builder<MacAddress> devices = ImmutableSet.builder();
        while (macAddressMatcher.find()) {
            final MacAddress mac = MacAddress.fromString(macAddressMatcher.group());
            devices.add(mac);
        }

        return devices.build();
    }

    public Map<String, Interface> getInterfaces() throws IOException {
        final String[] lines = execute(GET_NET_DEV_COMMAND).split("\\\\x0[ad]");

        // Skip the first 2 lines, they are headers
        final ImmutableMap.Builder<String, Interface> interfaces = ImmutableMap.builder();
        for (int row = 2; row < lines.length; row++) {
            Matcher matcher = NETDEV_TABLE_PATTERN.matcher(lines[row].trim());
            if (!matcher.matches()) continue;

            final String name = matcher.group(1);
            final List<Long> values = FluentIterable.from(NETDEV_FIELD_SPLITTER.split(matcher.group(2)))
                    .transform(Long::parseLong)
                    .toList();

            interfaces.put(name, new Interface(
                    new Interface.Stats(values.get(0), values.get(1), values.get(2), values.get(3)),
                    new Interface.Stats(values.get(8), values.get(9), values.get(10), values.get(11))
            ));
        }

        return interfaces.build();
    }
}
