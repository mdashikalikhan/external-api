package com.example.external_api.service;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class SessionManagementService {

    private final WebClient webClient;

    public SessionManagementService(WebClient webClient){
        this.webClient = webClient;
    }

    public Mono<List<Map<String, String>>> getLoggedInUsers() {
        return webClient.get().uri("/logged-in")
                .retrieve().bodyToMono(
                        new ParameterizedTypeReference<List<Map<String, String>>>() {
                        }
                )
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to fetch logged-in users", e)));
    }

    public Mono<String> logoutUser(String loginId) {
        return webClient.get()
                .uri("/logout/{loginId}", loginId)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to logout user: " + loginId, e)));
    }

}
