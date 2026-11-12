package ru.example.providerregistry.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import ru.example.providerregistry.model.Provider;
import ru.example.providerregistry.registry.InMemoryProviderRegistry;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class SubscriberAvailabilityServiceTest {
    private MockRestServiceServer server;
    private SubscriberAvailabilityService service;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        InMemoryProviderRegistry registry = new InMemoryProviderRegistry();
        registry.replaceAll(java.util.List.of(new Provider("provider-1", "https://provider.test", "secret")));
        service = new SubscriberAvailabilityService(registry, builder);
    }

    @Test
    void returnsTrueWhenProviderResponds200WithOkStatus() {
        server.expect(requestTo("https://provider.test/subscribers/%2B700000000000"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-API-Key", "secret"))
                .andRespond(withSuccess("{\"status\":\"ok\"}", MediaType.APPLICATION_JSON));

        assertTrue(service.isAvailable("provider-1", "+700000000000"));
        server.verify();
    }

    @Test
    void returnsFalseWhenProviderResponds404() {
        server.expect(requestTo("https://provider.test/subscribers/700000000000"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(org.springframework.http.HttpStatus.NOT_FOUND));

        assertFalse(service.isAvailable("provider-1", "700000000000"));
        server.verify();
    }
}
