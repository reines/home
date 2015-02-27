package com.furnaghan.home.registry;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.registry.config.ConfigurationStore;
import com.furnaghan.home.registry.config.FileConfigurationStore;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ComponentRegistryTest {

    public static class TestComponent extends Component<TestListener> implements TestType {}
    public static interface TestListener extends Component.Listener {}
    public static interface TestType extends ComponentType {}
    public static interface UnusedTestType extends ComponentType {}

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private ComponentRegistry registry;

    @Before
    public void setUp() throws IOException {
        final ConfigurationStore configuration = spy(new FileConfigurationStore(temporaryFolder.newFolder()));
        when(configuration.load(TestComponent.class)).thenReturn(ImmutableMap.of(
                "test1", Optional.absent(),
                "test2", Optional.absent()
        ));

        registry = new ComponentRegistry(configuration);
    }

    @Test
    public void testEmpty() {
        assertThat(registry.size(), is(0));
    }

    @Test(expected = NullPointerException.class)
    public void testLoadNullComponent() {
        registry.load(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetByNullName() {
        registry.getRegisteredComponent(null);
    }

    @Test
    public void testGetByEmptyName() {
        final Optional<ComponentType> result = registry.getRegisteredComponent("");
        assertThat(result.isPresent(), is(false));
    }

    @Test(expected = NullPointerException.class)
    public void testGetByNullType() {
        registry.getRegisteredComponents(null);
    }

    @Test
    public void testLoadComponent() {
        registry.load(TestComponent.class);
        assertThat(registry.size(), is(2));
    }

    @Test
    public void testNoComponentReturnsAbsent() {
        final Optional<ComponentType> result = registry.getRegisteredComponent("bananas");
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testUnknownComponentReturnsAbsent() {
        registry.load(TestComponent.class);
        final Optional<ComponentType> result = registry.getRegisteredComponent("bananas");
        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testComponentReturnedByName() {
        registry.load(TestComponent.class);
        final Optional<ComponentType> result = registry.getRegisteredComponent("test1");
        assertThat(result.isPresent(), is(true));
    }

    @Test
    public void testNoComponentReturnsEmptyCollection() {
        final Collection<TestType> result = registry.getRegisteredComponents(TestType.class);
        assertThat(result, is(empty()));
    }

    @Test
    public void testUnknownComponentReturnsEmptyCollection() {
        registry.load(TestComponent.class);
        final Collection<UnusedTestType> result = registry.getRegisteredComponents(UnusedTestType.class);
        assertThat(result, is(empty()));
    }

    @Test
    public void testComponentReturnsCollectionByType() {
        registry.load(TestComponent.class);
        final Collection<TestType> result = registry.getRegisteredComponents(TestType.class);
        assertThat(result.size(), is(2));
    }
}
