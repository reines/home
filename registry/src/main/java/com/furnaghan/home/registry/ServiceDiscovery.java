package com.furnaghan.home.registry;

import com.furnaghan.util.ReflectionUtil;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

public final class ServiceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);

    public static <T> Set<Class<T>> discoverServices(final Class<T> parent) {
        final ClassLoader classLoader = parent.getClassLoader();

        final Set<Class<T>> serviceClasses = Sets.newHashSet();
        try {
            final Enumeration<URL> resources = classLoader.getResources("META-INF/services/" + parent.getName());
            while (resources.hasMoreElements()) {
                final URL url = resources.nextElement();
                try (final Reader reader = new InputStreamReader(url.openStream())) {
                    final Collection<Class<T>> types = Collections2.transform(
                            CharStreams.readLines(reader), ReflectionUtil.classLoader(classLoader));
                    serviceClasses.addAll(types);
                }
            }
        }
        catch (IOException e) {
            logger.warn("Unable to load META-INF/services/{}", parent.getName(), e);
        }
        return Collections.unmodifiableSet( serviceClasses );
    }

    private ServiceDiscovery() {}
}
