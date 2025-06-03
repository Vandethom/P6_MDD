package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import com.openclassrooms.mddapi.repositories.ThemeRepository;
import com.openclassrooms.mddapi.repositories.UserRepository;
import com.openclassrooms.mddapi.security.AuthException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService implements ISubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            UserRepository         userRepository,
            ThemeRepository        themeRepository
            ) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository         = userRepository;
        this.themeRepository        = themeRepository;
    }

    @Override
    public List<Subscription> getUserSubscriptions(Long userId) {
        // Vérifier que l'utilisateur existe
        userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("Utilisateur non trouvé"));
                
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public Subscription subscribeToTheme(Long userId, Long themeId) {
        // Vérifier si l'utilisateur est déjà abonné à ce thème
        if (isUserSubscribedToTheme(userId, themeId)) {
            throw new AuthException("L'utilisateur est déjà abonné à ce thème");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("Utilisateur non trouvé"));
                
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new AuthException("Thème non trouvé"));
        
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setTheme(theme);
        
        return subscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void unsubscribeFromTheme(Long userId, Long themeId) {
        // Vérifier si l'abonnement existe
        Subscription subscription = subscriptionRepository.findByUserIdAndThemeId(userId, themeId)
                .orElseThrow(() -> new AuthException("Abonnement non trouvé"));
        
        // Supprimer l'abonnement
        subscriptionRepository.delete(subscription);
    }

    @Override
    public boolean isUserSubscribedToTheme(Long userId, Long themeId) {
        return subscriptionRepository.findByUserIdAndThemeId(userId, themeId).isPresent();
    }
}
