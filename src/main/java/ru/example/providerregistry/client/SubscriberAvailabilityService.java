package ru.example.providerregistry.client;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.example.providerregistry.model.Provider;
import ru.example.providerregistry.model.SubscriberResponse;
import ru.example.providerregistry.registry.ProviderRegistry;

@Service
public class SubscriberAvailabilityService {
    private final ProviderRegistry registry;
    private final RestClient restClient;

    public SubscriberAvailabilityService(ProviderRegistry registry, RestClient.Builder restClientBuilder) {
        this.registry = registry;
        this.restClient = restClientBuilder.build();
    }

    public boolean isAvailable(String providerId, String phone) {
        Provider provider = registry.findById(providerId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown provider: " + providerId
                ));

        try {
            SubscriberResponse response = restClient.get()
                    .uri(provider.baseUrl() + "/subscribers/{phone}", phone)
                    .header("X-API-Key", provider.apiKey())
                    .retrieve()
                    .body(SubscriberResponse.class);

            return response != null && "ok".equals(response.status());

        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
