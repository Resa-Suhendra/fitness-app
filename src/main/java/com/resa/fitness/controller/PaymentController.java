package com.resa.fitness.controller;

import com.resa.fitness.entity.User;
import com.resa.fitness.model.PaymentResponse;
import com.resa.fitness.model.RequestPayments;
import com.resa.fitness.model.WebResponse;
import com.resa.fitness.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Resa S.
 * Date: 22-10-2023
 * Created in IntelliJ IDEA.
 */
@RestController
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @PostMapping(
            path = "/api/payment/request",
            consumes = "application/json",
            produces = "application/json"
    )
    public WebResponse<PaymentResponse> request(User user, @RequestBody RequestPayments requestPayments) {
        try {
            PaymentResponse response = paymentService.request(user, requestPayments);
            return WebResponse.<PaymentResponse>builder().data(response).build();
        } catch (Exception e) {
            return WebResponse.<PaymentResponse>builder().data(null).build();
        }
    }

    @PatchMapping(
            path = "/api/payment/confirm",
            consumes = "application/json",
            produces = "application/json"
    )
    public WebResponse<String> confirm(User user, @RequestBody RequestPayments requestPayments) {
        try {
            paymentService.confirm(user, requestPayments);
            return WebResponse.<String>builder().data("Payment confirm success").build();
        } catch (Exception e) {
            return WebResponse.<String>builder().data("Payment confirm failed: " + e.getMessage()).build();
        }
    }

    @GetMapping(
            path = "/api/payment/send-otp",
            produces = "application/json"
    )
    public WebResponse<PaymentResponse> sendOtp(User user, @RequestParam String paymentId) {
        try {
            PaymentResponse response = paymentService.sendOtp(user, paymentId);
            return WebResponse.<PaymentResponse>builder().data(response).build();
        } catch (Exception e) {
            return WebResponse.<PaymentResponse>builder().data(null).build();
        }
    }

    @GetMapping(
            path = "/api/payment/status",
            produces = "application/json"
    )
    public WebResponse<String> status(User user, @RequestParam String paymentId) {
        try {
            String response = paymentService.status(user, paymentId);
            return WebResponse.<String>builder().data(response).build();
        } catch (Exception e) {
            return WebResponse.<String>builder().data(null).build();
        }
    }


}
