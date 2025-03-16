package com.example.ApiGateway.config;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//@Component
//public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {
//
//    @Autowired
//    private WebClient.Builder webClientBuilder; // For non-blocking HTTP calls
//
//    public CustomAuthFilter() {
//        super(Config.class);
//    }
//
//    public static class Config {}
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            // Allow OPTIONS requests without authentication (CORS preflight)
//            if (exchange.getRequest().getMethod().name().equalsIgnoreCase("OPTIONS")) {
//                return chain.filter(exchange);
//            }
//
//            // Retrieve the Authorization header
//            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                return Mono.error(new RuntimeException("Missing or invalid Authorization header"));
//            }
//
//            // Extract the JWT token from the header
//            String token = authHeader.substring(7);
//
//            // Call the Auth Server to validate the token
//            return webClientBuilder.build()
//                    .get()
//                    .uri("http://localhost:8082/auth/validateToken?token=" + token)
//                    .retrieve()
//                    .bodyToMono(Boolean.class)
//                    .flatMap(isValid -> {
//                        if (Boolean.TRUE.equals(isValid)) {
//                            // If valid, continue processing the request
//                            return chain.filter(exchange);
//                        } else {
//                            // Otherwise, return an error
//                            return Mono.error(new RuntimeException("Invalid JWT Token"));
//                        }
//                    })
//                    .onErrorResume(e -> {
//                        // In case of any error during token validation, return 401
//                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                        return exchange.getResponse().setComplete();
//                    });
//        };
//    }
//}

import com.example.ApiGateway.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {

    @Autowired
    private WebClient.Builder webClientBuilder; // For non-blocking HTTP calls

    @Autowired
    private JWTUtils jwtUtils;

    public CustomAuthFilter() {
        super(Config.class);
    }

    public static class Config {}

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Allow OPTIONS requests without authentication (CORS preflight)
            if (exchange.getRequest().getMethod().name().equalsIgnoreCase("OPTIONS")) {
                return chain.filter(exchange);
            }

            // Try to extract the token from a cookie using reactive API
            String token = getJwtFromCookie(exchange);

            // If token not found in cookies, fallback to Authorization header
            if (token == null) {
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                }
            }

            if (token == null) {
                return Mono.error(new RuntimeException("Missing or invalid Authorization header"));
            }

            // Call the Auth Server to validate the token
            return webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/auth/validateToken?token=" + token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .flatMap(isValid -> {
                        if (Boolean.TRUE.equals(isValid)) {
                            // If valid, continue processing the request
                            return chain.filter(exchange);
                        } else {
                            return Mono.error(new RuntimeException("Invalid JWT Token"));
                        }
                    })
                    .onErrorResume(e -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        };
    }

    // Use reactive API to extract cookie
    private String getJwtFromCookie(ServerWebExchange exchange) {
        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
        if (cookies != null && cookies.containsKey("jwt")) {
            HttpCookie jwtCookie = cookies.getFirst("jwt");
            if (jwtCookie != null) {
                return jwtCookie.getValue();
            }
        }
        return null;
    }

    public int getOrder() {
        return -1; // Run early in the filter chain
    }
}
