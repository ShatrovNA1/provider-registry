package ru.example.providerregistry.model;

import java.net.URI;
import java.util.Objects;

public record Provider(String id, String baseUrl, String apiKey) {
    public Provider {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Provider id must not be blank");
        if (baseUrl == null || baseUrl.isBlank()) throw new IllegalArgumentException("Provider baseUrl must not be blank");
        if (apiKey == null || apiKey.isBlank()) throw new IllegalArgumentException("Provider apiKey must not be blank");
        URI uri = URI.create(baseUrl);
        if (!Objects.equals(uri.getScheme(), "http") && !Objects.equals(uri.getScheme(), "https")) {
            throw new IllegalArgumentException("Provider baseUrl must use http or https");
        }
        if (uri.getHost() == null) throw new IllegalArgumentException("Provider baseUrl must contain a host");
        baseUrl = baseUrl.replaceAll("/+$", "");
    }
}
