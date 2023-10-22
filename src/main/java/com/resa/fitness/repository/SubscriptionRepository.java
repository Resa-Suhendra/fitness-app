package com.resa.fitness.repository;

import com.resa.fitness.entity.ServiceMenu;
import com.resa.fitness.entity.Subscription;
import com.resa.fitness.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    public Optional<Subscription> findByUserAndServiceMenu(User user, ServiceMenu serviceMenu);
    public Optional<Subscription> findByUserAndServiceMenuAndActive(User user, ServiceMenu serviceMenu, boolean active);
}
