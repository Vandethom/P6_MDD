package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;

import java.util.List;

public interface ISubscriptionService {
    List<Subscription> getUserSubscriptions(Long userId);
    Subscription       subscribeToTheme(Long userId, Long themeId);
    void               unsubscribeFromTheme(Long userId, Long themeId);
    boolean            isUserSubscribedToTheme(Long userId, Long themeId);
}
