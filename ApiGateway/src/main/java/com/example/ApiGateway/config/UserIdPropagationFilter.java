package com.example.ApiGateway.config;

//import com.example.ApiGateway.utils.JWTUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class UserIdPropagationFilter implements GlobalFilter, Ordered {
//    @Autowired
//    private JWTUtils jwtUtils;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String token = null;
//        // Try to extract the token from a cookie:
//        if (exchange.getRequest().getCookies().getFirst("jwt") != null) {
//            token = exchange.getRequest().getCookies().getFirst("jwt").getValue();
//        }
//        // Fallback to the Authorization header if needed:
//        if (token == null) {
//            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                token = authHeader.substring(7);
//            }
//        }
//
//        if (token != null && jwtUtils.validateToken(token)) {
//            // Extract userId from the custom claim
//            String userId = jwtUtils.getUserIdFromToken(token);
//            // Mutate the exchange to add the userId header
//            ServerWebExchange modifiedExchange = exchange.mutate()
//                    .request(r -> r.header("X-User-Id", userId))
//                    .build();
//            return chain.filter(modifiedExchange);
//        }
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return -1;
//    }
//}

import com.example.ApiGateway.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpCookie;

@Component
public class UserIdPropagationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = null;

        // Try to extract the token from a cookie:
        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
        cookies.forEach((name, values) -> {
            System.out.println("Cookie Name: " + name);
            values.forEach(cookie -> System.out.println("Cookie Value: " + cookie.getValue()));
        });

        if (cookies != null && cookies.containsKey("jwt")) {
            token = cookies.getFirst("jwt").getValue();
        }

        // Fallback to the Authorization header if needed:
        if (token == null) {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        if (token != null && jwtUtils.validateToken(token)) {
            // Extract userId from the custom claim
            String userId = jwtUtils.getUserIdFromToken(token);
            // Optionally, add logging for debugging:
            // System.out.println("Extracted X-User-Id: " + userId);
            // Mutate the exchange to add the userId header
            System.out.println(userId);
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(r -> r.header("X-User-Id", userId))
                    .build();
            return chain.filter(modifiedExchange);
        }

        // If token is not present or not valid, you might decide to return an error or simply pass along:
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Run early in the filter chain
    }
}
