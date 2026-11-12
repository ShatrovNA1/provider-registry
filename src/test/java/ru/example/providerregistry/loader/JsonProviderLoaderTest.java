package ru.example.providerregistry.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.example.providerregistry.registry.InMemoryProviderRegistry;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class JsonProviderLoaderTest {
    @TempDir Path tempDir;

    @Test
    void loadsProvidersFromJsonAndReplacesRegistry() throws Exception {
        Path file = tempDir.resolve("providers.json");
        Files.writeString(file, """
                [{"id":"p1","baseUrl":"https://p1.test","apiKey":"secret"}]
                """);
        InMemoryProviderRegistry registry = new InMemoryProviderRegistry();
        JsonProviderLoader loader = new JsonProviderLoader(new ObjectMapper());
        loader.load(file, registry);
        assertEquals("https://p1.test", registry.findById("p1").orElseThrow().baseUrl());
    }
}
