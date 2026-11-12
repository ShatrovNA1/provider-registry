package ru.example.providerregistry.registry;

import ru.example.providerregistry.model.Provider;
import java.util.List;
import java.util.Optional;

public interface ProviderRegistry {
    Optional<Provider> findById(String providerId);
    void replaceAll(List<Provider> providers);
    List<Provider> findAll();
}
