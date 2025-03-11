package com.example.ApiGateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public CustomAuthFilter() {
        super(Config.class);
    }

    public static class Config {
        // Configuration properties if needed
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String isAuthenticated = exchange.getRequest().getHeaders().getFirst("isAuthenticated");

            if ("true".equalsIgnoreCase(isAuthenticated)) {
                // Perform JWT validation
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return Mono.error(new RuntimeException("Missing or invalid Authorization header"));
                }

                String token = authHeader.substring(7);
                // Call Auth Service to validate the token
                return webClientBuilder.build()
                        .get()
                        .uri("http://localhost:8082/validateToken?token=" + token)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .flatMap(isValid -> {
                            if (Boolean.TRUE.equals(isValid)) {
                                return chain.filter(exchange);
                            } else {
                                return Mono.error(new RuntimeException("Invalid JWT Token"));
                            }
                        });
            } else {
                // No authentication required
                return chain.filter(exchange);
            }
        };
    }
}
