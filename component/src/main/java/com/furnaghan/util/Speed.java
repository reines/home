package com.furnaghan.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.util.SizeUnit;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Speed {
    private static final Pattern SPEED_PATTERN = Pattern.compile("(\\d+)\\s*(\\S+)");

    private static final Map<String, SizeUnit> SUFFIXES = new ImmutableMap.Builder<String, SizeUnit>()
            .put("bps", SizeUnit.BYTES)
            .put("kbps", SizeUnit.KILOBYTES)
            .put("mbps", SizeUnit.MEGABYTES)
            .put("gbps", SizeUnit.GIGABYTES)
            .put("tbps", SizeUnit.TERABYTES)
            .build();

    public static Speed bytes(long count) {
        return new Speed(count, SizeUnit.BYTES);
    }

    public static Speed kilobytes(long count) {
        return new Speed(count, SizeUnit.KILOBYTES);
    }

    public static Speed megabytes(long count) {
        return new Speed(count, SizeUnit.MEGABYTES);
    }

    public static Speed gigabytes(long count) {
        return new Speed(count, SizeUnit.GIGABYTES);
    }

    public static Speed terabytes(long count) {
        return new Speed(count, SizeUnit.TERABYTES);
    }

    @JsonCreator
    public static Speed parse(String speed) {
        final Matcher matcher = SPEED_PATTERN.matcher(speed);
        checkArgument(matcher.matches(), "Invalid speed: " + speed);

        final long count = Long.valueOf(matcher.group(1));
        final SizeUnit unit = SUFFIXES.get(matcher.group(2));
        if (unit == null) {
            throw new IllegalArgumentException("Invalid speed: " + speed + ". Wrong size unit");
        }

        return new Speed(count, unit);
    }

    private final long count;
    private final SizeUnit unit;

    private Speed(long count, SizeUnit unit) {
        this.count = count;
        this.unit = checkNotNull(unit);
    }

    public long getQuantity() {
        return count;
    }

    public SizeUnit getUnit() {
        return unit;
    }

    public long toBytes() {
        return SizeUnit.BYTES.convert(count, unit);
    }

    public long toKilobytes() {
        return SizeUnit.KILOBYTES.convert(count, unit);
    }

    public long toMegabytes() {
        return SizeUnit.MEGABYTES.convert(count, unit);
    }

    public long toGigabytes() {
        return SizeUnit.GIGABYTES.convert(count, unit);
    }

    public long toTerabytes() {
        return SizeUnit.TERABYTES.convert(count, unit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        final Speed size = (Speed) obj;
        return (count == size.count) && (unit == size.unit);
    }

    @Override
    public int hashCode() {
        return (31 * (int) (count ^ (count >>> 32))) + unit.hashCode();
    }

    @Override
    @JsonValue
    public String toString() {
        String units = unit.toString().toLowerCase(Locale.ENGLISH);
        if (count == 1) {
            units = units.substring(0, units.length() - 1);
        }
        return String.format("%d %s/sec", count, units);
    }
}
