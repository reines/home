package com.furnaghan.home.policy.util;

import com.furnaghan.home.policy.Policy;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PolicyFunnel implements Funnel<Policy> {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    @Override
    public void funnel(Policy policy, PrimitiveSink into) {
        into.putString(policy.getType().toString(), CHARSET)
                .putString(policy.getEvent(), CHARSET)
                .putString(policy.getScript(), CHARSET);

        for (Class<?> type : policy.getParameterTypes()) {
            into.putString(type.toString(), CHARSET);
        }
    }
}
