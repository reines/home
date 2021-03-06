package com.furnaghan.home.policy.store.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnaghan.home.policy.Policy;
import com.furnaghan.home.policy.store.PolicyStore;
import com.furnaghan.home.policy.util.PolicyFunnel;
import com.furnaghan.home.util.JsonUtil;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.hash.Funnel;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;

public class FilePolicyStore extends PolicyStore {

    private static final ObjectMapper JSON = JsonUtil.newObjectMapper();
    private static final String FILE_EXT = "json";
    private static final HashFunction HASH_FUNCTION = Hashing.md5();
    private static final Funnel<Policy> POLICY_FUNNEL = new PolicyFunnel();

    private final File path;

    public FilePolicyStore(final File path) {
        this.path = path;
        checkArgument(path.isDirectory() || path.mkdirs(), "Unable to create directory: " + path.getAbsolutePath());
    }

    private File policyFile(final Policy policy) {
        final HashCode hash = HASH_FUNCTION.hashObject(policy, POLICY_FUNNEL);
        return new File(path, String.format("%s.%s", hash, FILE_EXT));
    }

    @Override
    public Collection<Policy> load() {
        try {
            final ImmutableSet.Builder<Policy> result = ImmutableSet.builder();
            Files.list(path.toPath()).forEach(path -> {
                final File file = path.toFile();
                final Policy policy = load(file);

                trigger(l -> l.onPolicyAdded(policy));
                result.add(policy);
            });
            return result.build();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void save(final Policy policy) {
        final File file = policyFile(policy);
        try {
            JSON.writeValue(file, policy);
            trigger(l -> l.onPolicyAdded(policy));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public boolean delete(final Policy policy) {
        final File file = policyFile(policy);
        if (file.delete()) {
            trigger(l -> l.onPolicyRemoved(policy));
            return true;
        }

        return false;
    }

    private Policy load(final File file) {
        try {
            return JSON.readValue(file, Policy.class);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
