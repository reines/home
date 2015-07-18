package com.furnaghan.home.component.feed.rss.client.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnaghan.home.util.JsonUtil;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.function.Predicate;

public class SeenItemFilter<T> implements Predicate<T> {

    private static final HashFunction HASH_FUNCTION = Hashing.sha1();
    private static final ObjectMapper JSON = JsonUtil.newObjectMapper();

    private final int maxSize;
    private final LinkedHashSet<HashCode> seen;
    private final Funnel<T> funnel;

    public SeenItemFilter(final int maxSize) {
        this.maxSize = maxSize;

        seen = new LinkedHashSet<>();
        funnel = JsonUtil.jsonFunnel(JSON);
    }

    @Override
    public boolean test(final T input) {
        if (input == null) {
            return false;
        }

        final Iterator<HashCode> iterator = seen.iterator();
        while (seen.size() >= maxSize && iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }

        // Returns true if the set didn't already contain the item
        final HashCode hash = HASH_FUNCTION.hashObject(input, funnel);
        return seen.add(hash);
    }
}
