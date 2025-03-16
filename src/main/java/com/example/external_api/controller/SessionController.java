package com.example.external_api.controller;

import com.example.external_api.service.SessionManagementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final SessionManagementService sessionService;

    public SessionController(SessionManagementService sessionService) {
        this.sessionService = sessionService;
    }
    @GetMapping("/logged-in-users")
    public Mono<List<Map<String, String>>> getLoggedInUsers() {
        return sessionService.getLoggedInUsers();
    }

    @GetMapping("/logout/{loginId}")
    public Mono<String> logoutUser(@PathVariable String loginId) {
        return sessionService.logoutUser(loginId);
    }
}
