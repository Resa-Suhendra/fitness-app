package com.resa.fitness.service;

import com.resa.fitness.entity.ServiceMenu;
import com.resa.fitness.entity.Subscription;
import com.resa.fitness.entity.User;
import com.resa.fitness.model.SubscriptionRequest;
import com.resa.fitness.repository.ServiceMenuRepository;
import com.resa.fitness.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Created by Resa S.
 * Date: 22-10-2023
 * Created in IntelliJ IDEA.
 */
@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    private ServiceMenuRepository serviceMenuRepository;

    public void subscribe(User user, SubscriptionRequest subscriptionRequest) {
        Optional<ServiceMenu> serviceMenuOptional = serviceMenuRepository.findById(subscriptionRequest.getServiceMenuId());
        if (serviceMenuOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service menu not found");
        }

        Optional<Subscription> subscriptionOptional = subscriptionRepository.findByUserAndServiceMenuAndActive(user, serviceMenuOptional.get(), true);
        if (subscriptionOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already subscribed");
        }

        Subscription subscription = new Subscription();
        subscription.setServiceMenu(serviceMenuOptional.get());
        subscription.setRemainingSessions(serviceMenuOptional.get().getTotalSessions());
        subscription.setTotalSessions(serviceMenuOptional.get().getTotalSessions());
        subscription.setActive(false);
        subscription.setStartDate(subscriptionRequest.getStartDate());
        subscription.setEndDate(subscriptionRequest.getEndDate());
        subscription.setUser(user);

        subscriptionRepository.save(subscription);
    }

    public void cancel(User user, SubscriptionRequest subscriptionRequest) {
        Optional<ServiceMenu> serviceMenuOptional = serviceMenuRepository.findById(subscriptionRequest.getServiceMenuId());
        if (serviceMenuOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service menu not found");
        }

        Optional<Subscription> subscriptionOptional = subscriptionRepository.findByUserAndServiceMenuAndActive(user, serviceMenuOptional.get(), true);
        if (subscriptionOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not subscribed");
        }

        Subscription subscription = subscriptionOptional.get();
        subscriptionRepository.delete(subscription);
    }

    public void extend(User user, SubscriptionRequest subscriptionRequest) {
        Optional<ServiceMenu> serviceMenuOptional = serviceMenuRepository.findById(subscriptionRequest.getServiceMenuId());
        if (serviceMenuOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service menu not found");
        }

        Optional<Subscription> subscriptionOptional = subscriptionRepository.findByUserAndServiceMenuAndActive(user, serviceMenuOptional.get(), true);
        if (subscriptionOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not subscribed");
        }

        Subscription subscription = subscriptionOptional.get();
        subscription.setEndDate(subscriptionRequest.getEndDate());
        subscription.setTotalSessions(subscription.getTotalSessions() + serviceMenuOptional.get().getTotalSessions());
        subscription.setRemainingSessions(subscription.getRemainingSessions() + serviceMenuOptional.get().getTotalSessions());
        subscriptionRepository.save(subscription);
    }
}
