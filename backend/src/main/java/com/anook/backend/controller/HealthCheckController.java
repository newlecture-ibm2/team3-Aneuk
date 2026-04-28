package com.anook.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@RestController
public class HealthCheckController {

    @GetMapping("/api/health")
    public String healthCheck() {
        return "Backend is running successfully! 🚀";
    }

    @GetMapping("/api/ai-status")
    public String aiStatusCheck() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String aiUrl = "http://ai:8000/api/v1/test-ai";
            ResponseEntity<String> response = restTemplate.getForEntity(aiUrl, String.class);
            return "✅ Backend successfully communicated with hidden AI server! AI Response: " + response.getBody();
        } catch (Exception e) {
            return "❌ Failed to reach hidden AI server from Backend. Error: " + e.getMessage();
        }
    }
}
