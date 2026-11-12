package ru.example.providerregistry.registry;

import org.junit.jupiter.api.Test;
import ru.example.providerregistry.model.Provider;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryProviderRegistryTest {
    @Test
    void replaceAllReplacesTheWholeList() {
        InMemoryProviderRegistry registry = new InMemoryProviderRegistry();
        Provider first = new Provider("first", "https://first.test", "key1");
        Provider second = new Provider("second", "https://second.test", "key2");
        registry.replaceAll(List.of(first, second));
        registry.replaceAll(List.of(second));
        assertEquals(List.of(second), registry.findAll());
        assertTrue(registry.findById("first").isEmpty());
    }

    @Test
    void rejectsDuplicateIdsWithoutChangingCurrentState() {
        InMemoryProviderRegistry registry = new InMemoryProviderRegistry();
        Provider existing = new Provider("existing", "https://existing.test", "key");
        registry.replaceAll(List.of(existing));
        assertThrows(IllegalArgumentException.class, () -> registry.replaceAll(List.of(
                new Provider("duplicate", "https://one.test", "1"),
                new Provider("duplicate", "https://two.test", "2"))));
        assertEquals(List.of(existing), registry.findAll());
    }

    @Test
    void nullIdYieldsEmptyOptional() {
        InMemoryProviderRegistry registry = new InMemoryProviderRegistry();
        registry.replaceAll(List.of(new Provider("a", "https://a.test", "k")));
        assertTrue(registry.findById(null).isEmpty());
    }
}
