package com.furnaghan.home.component.router.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.regex.Pattern;

public class MacAddress {

    public static final Pattern PATTERN = Pattern.compile("(?:[0-9A-F]{2}[:-]){5}(?:[0-9A-F]{2})", Pattern.CASE_INSENSITIVE);

    @JsonCreator
    public static MacAddress fromString(String address) {
        return new MacAddress(address);
    }

    public static String normalize(String address) {
        return address.toLowerCase().replaceAll("-", ":");
    }

    public static boolean validate(String address) {
        return PATTERN.matcher(address).matches();
    }

    private final String address;

    private MacAddress(final String address) {
        if (!validate(address)) {
            throw new IllegalArgumentException("Invalid MAC address: " + address);
        }

        this.address = normalize(address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MacAddress that = (MacAddress) o;

        if (address != null ? !address.equals(that.address) : that.address != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return address != null ? address.hashCode() : 0;
    }

    @JsonValue
    @Override
    public String toString() {
        return address;
    }
}
