package com.furnaghan.home.component.ping.pinger;

import com.furnaghan.home.component.ping.PingResult;
import com.google.common.io.CharStreams;
import io.dropwizard.util.Duration;
import io.dropwizard.util.Size;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkState;

public abstract class UnixPinger implements Pinger {

    private static final Pattern RESULT_PATTERN = Pattern.compile(
            "(?<size>\\d+) bytes from.*ttl=(?<ttl>\\d+).*time=(?<time>\\d*(?:\\.\\d*)?) ms",
            Pattern.DOTALL | Pattern.MULTILINE);

    private static String readStream(final InputStream stream) throws IOException {
        try (final Reader reader = new InputStreamReader(stream)) {
            return CharStreams.toString(reader).trim();
        }
    }

    private final Duration timeout;

    public UnixPinger(final Duration timeout) {
        this.timeout = timeout;
    }

    @Override
    public PingResult ping(final InetAddress address) throws IOException, InterruptedException {
        final Process process = new ProcessBuilder(command(address, timeout)).start();
        final int status = process.waitFor();
        if (status != 0) {
            final String error = readStream(process.getErrorStream());
            handleError(error.startsWith("ping:") ? error.substring(5) : error);
        }

        return parse(readStream(process.getInputStream()));
    }

    protected abstract List<String> command(final InetAddress address, final Duration timeout);

    private void handleError(final String error) throws IOException {
        if (error.startsWith("invalid")) {
            throw new IllegalArgumentException(error);
        }

        throw new IOException(error);
    }

    private PingResult parse(final String result) {
        final Matcher matcher = RESULT_PATTERN.matcher(result);
        checkState(matcher.find(), "Unrecognised ping result format: " + result);

        return new PingResult(
                Duration.microseconds((long) (Double.parseDouble(matcher.group("time")) * 1000D)),
                Integer.parseInt(matcher.group("ttl")),
                Size.bytes(Long.parseLong(matcher.group("size")))
        );
    }
}
