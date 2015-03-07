package com.furnaghan.home.policy.store;

import com.google.common.base.Optional;
import com.google.common.io.ByteSource;
import com.google.common.io.Resources;

public class ResourceScriptStore extends ScriptStore {

    private static String ensureTrailingSlash(final String path) {
        return path.endsWith("/") ? path : path + "/";
    }

    private final String prefix;

    public ResourceScriptStore(final String prefix) {
        this.prefix = ensureTrailingSlash(prefix);
    }

    @Override
    protected Optional<ByteSource> open(final String name) {
        return Optional.fromNullable(ResourceScriptStore.class.getResource(prefix + name))
                .transform(Resources::asByteSource);
    }
}
