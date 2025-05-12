package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription>     findByUserId(Long userId);
    Optional<Subscription> findByUserIdAndThemeId(Long userId, Long themeId);
    void                   deleteByUserIdAndThemeId(Long userId, Long themeId);
}
