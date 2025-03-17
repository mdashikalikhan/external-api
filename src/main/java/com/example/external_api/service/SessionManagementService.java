package com.example.external_api.service;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse ->
                                Mono.empty())
                        //Mono.error(new RuntimeException("Client Error: " + clientResponse.statusCode())))
                .bodyToMono(
                        new ParameterizedTypeReference<List<Map<String, String>>>() {
                        }
                )
                /*.onErrorResume(WebClientResponseException.class, ex->{
                    if(ex.getStatusCode()== HttpStatus.NOT_FOUND){
                        return Mono.error(new RuntimeException("No logged-in users found."));
                    } else if (ex.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                        return Mono.error(new RuntimeException("External service is unavailable (503)"));
                    }
                    return Mono.error(new RuntimeException("Unexpected error: " + ex.getMessage()));
                })*/;
    }

    public Mono<String> logoutUser(String loginId) {
        return webClient.get()
                .uri("/logout/{loginId}", loginId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse ->
                                Mono.empty())
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("Failed to logout user: " + loginId, e)));
    }

}
