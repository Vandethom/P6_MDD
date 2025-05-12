package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.dto.SubscriptionResponse;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.services.ISubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class SubscriptionController {

    private final ISubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(ISubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getUserSubscriptions(@PathVariable Long userId) {
        List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(userId);
        List<SubscriptionResponse> responses = subscriptions.stream()
                .map(SubscriptionResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{userId}/subscriptions")
    public ResponseEntity<SubscriptionResponse> subscribeToTheme(
            @PathVariable Long userId,
            @RequestBody Map<String, Long> payload) {
        Long         themeId      = payload.get("themeId");
        Subscription subscription = subscriptionService.subscribeToTheme(userId, themeId);

        return ResponseEntity.ok(SubscriptionResponse.fromEntity(subscription));
    }

    @DeleteMapping("/{userId}/subscriptions/{themeId}")
    public ResponseEntity<Void> unsubscribeFromTheme(
            @PathVariable Long userId,
            @PathVariable Long themeId
            ) {
        subscriptionService.unsubscribeFromTheme(userId, themeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/subscriptions/{themeId}")
    public ResponseEntity<Boolean> checkSubscription(
            @PathVariable Long userId,
            @PathVariable Long themeId) {
        boolean isSubscribed = subscriptionService.isUserSubscribedToTheme(userId, themeId);
        
        return ResponseEntity.ok(isSubscribed);
    }
}
