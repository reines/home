package com.furnaghan.home.component.feed.rss.client.filter;

import com.furnaghan.home.component.feed.rss.client.util.JsonByteFunnel;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.function.Predicate;

public class SeenItemFilter<T> implements Predicate<T> {

    private static final HashFunction HASH_FUNCTION = Hashing.sha1();

    private final int maxSize;
    private final LinkedHashSet<HashCode> seen;
    private final Funnel<T> funnel;

    public SeenItemFilter(final int maxSize) {
        this.maxSize = maxSize;

        seen = new LinkedHashSet<>();
        funnel = new JsonByteFunnel<>();
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
