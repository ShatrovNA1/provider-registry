package ru.example.providerregistry.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.example.providerregistry.model.Provider;
import ru.example.providerregistry.registry.ProviderRegistry;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class JsonProviderLoader {
    private final ObjectMapper objectMapper;

    public JsonProviderLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /** Reads the complete provider list and replaces the current registry atomically. */
    public void load(Path file, ProviderRegistry registry) throws IOException {
        List<Provider> providers = objectMapper.readValue(file.toFile(), new TypeReference<>() {});
        registry.replaceAll(providers);
    }
}
