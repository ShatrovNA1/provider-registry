package ru.example.providerregistry.registry;

import org.springframework.stereotype.Component;
import ru.example.providerregistry.model.Provider;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class InMemoryProviderRegistry implements ProviderRegistry {
    private final AtomicReference<Map<String, Provider>> providers = new AtomicReference<>(Map.of());

    @Override
    public Optional<Provider> findById(String providerId) {
        if (providerId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(providers.get().get(providerId));
    }

    @Override
    public void replaceAll(List<Provider> newProviders) {
        Objects.requireNonNull(newProviders, "providers must not be null");
        Map<String, Provider> replacement = new LinkedHashMap<>();
        for (Provider provider : newProviders) {
            Objects.requireNonNull(provider, "provider must not be null");
            if (replacement.putIfAbsent(provider.id(), provider) != null) {
                throw new IllegalArgumentException("Duplicate provider id: " + provider.id());
            }
        }
        providers.set(Map.copyOf(replacement));
    }

    @Override
    public List<Provider> findAll() {
        return List.copyOf(providers.get().values());
    }
}
