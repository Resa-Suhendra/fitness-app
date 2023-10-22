package com.resa.fitness.controller;

import com.resa.fitness.entity.User;
import com.resa.fitness.model.SubscriptionRequest;
import com.resa.fitness.model.WebResponse;
import com.resa.fitness.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Resa S.
 * Date: 22-10-2023
 * Created in IntelliJ IDEA.
 */
@RestController
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping(path = "/api/subscription/subscribe", consumes = "application/json", produces = "application/json")
    public WebResponse<String> subscribe(User user, @RequestBody SubscriptionRequest subscriptionRequest) {
        try {
            subscriptionService.subscribe(user,subscriptionRequest);
            return WebResponse.<String>builder().data("Subscription success, please continue to payment").build();
        } catch (Exception e) {
            return WebResponse.<String>builder().data("Subscription failed: " + e.getMessage()).build();
        }
    }

    @DeleteMapping(path = "/api/subscription/cancel", consumes = "application/json", produces = "application/json")
    public WebResponse<String> cancel(User user, @RequestBody SubscriptionRequest subscriptionRequest) {
        try {
            subscriptionService.cancel(user,subscriptionRequest);
            return WebResponse.<String>builder().data("Cancel subscription success").build();
        } catch (Exception e) {
            return WebResponse.<String>builder().data("Cancel subscription failed: " + e.getMessage()).build();
        }
    }

    @PatchMapping(path = "/api/subscription/extend", consumes = "application/json", produces = "application/json")
    public WebResponse<String> extend(User user, @RequestBody SubscriptionRequest subscriptionRequest) {
        try {
            subscriptionService.extend(user,subscriptionRequest);
            return WebResponse.<String>builder().data("Extend subscription success").build();
        } catch (Exception e) {
            return WebResponse.<String>builder().data("Extend subscription failed: " + e.getMessage()).build();
        }
    }
}
