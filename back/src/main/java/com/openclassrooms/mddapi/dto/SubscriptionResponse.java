package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponse {
    private Long          id;
    private Long          userId;
    private Long          themeId;
    private Theme         theme;
    private LocalDateTime createdAt;
    
    public static SubscriptionResponse fromEntity(Subscription subscription) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(subscription.getId());
        response.setUserId(subscription.getUser().getId());
        response.setThemeId(subscription.getTheme().getId());
        response.setTheme(subscription.getTheme());
        response.setCreatedAt(subscription.getCreatedAt());
        return response;
    }
}
