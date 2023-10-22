package com.resa.fitness.service;

import com.resa.fitness.entity.Payment;
import com.resa.fitness.entity.ServiceMenu;
import com.resa.fitness.entity.Subscription;
import com.resa.fitness.entity.User;
import com.resa.fitness.model.PaymentResponse;
import com.resa.fitness.model.RequestPayments;
import com.resa.fitness.repository.PaymentRepository;
import com.resa.fitness.repository.ServiceMenuRepository;
import com.resa.fitness.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Resa S.
 * Date: 22-10-2023
 * Created in IntelliJ IDEA.
 */
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    ServiceMenuRepository serviceMenuRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public PaymentResponse request(User user, RequestPayments requestPayments) {
        ServiceMenu serviceMenu = serviceMenuRepository.findById(requestPayments.getServiceMenuId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service menu not found"));
        final Optional<Subscription> subscriptionOptional = subscriptionRepository.findByUserAndServiceMenuAndActive(user, serviceMenu, false);

        if (subscriptionOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription not found");
        }

        Subscription subscription = subscriptionOptional.get();

        Payment payment = new Payment();
        payment.setPaymentTime(LocalDateTime.now());

        double bill = serviceMenu.getPricePerSession() * serviceMenu.getTotalSessions();
        payment.setId(UUID.randomUUID().toString());
        payment.setAmount(bill);
        payment.setPaid(false);
        payment.setOtpCode("123456");
        payment.setOtpExpiration(next30Minutes());

        payment.setUser(user);
        payment.setServiceMenu(serviceMenu);
        payment.setSubscription(subscription);

        paymentRepository.save(payment);
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .serviceMenuId(serviceMenu.getId())
                .subscriptionId(subscription.getId())
                .status(payment.isPaid() ? "PAID" : "UNPAID")
                .otpCode(payment.getOtpCode())
                .otpExpiration(payment.getOtpExpiration())
                .message("Payment request success, OTP code has been sent to your email")
                .build();
    }


    public void confirm(User user, RequestPayments requestPayments) {
        Optional<Payment> paymentOptional = paymentRepository.findById(requestPayments.getPaymentId());
        if (paymentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }

        Payment payment = paymentOptional.get();
        if (payment.isPaid()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment already paid");
        }

        if (!payment.getOtpCode().equals(requestPayments.getOtpCode())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid OTP code");
        }

        if (payment.getOtpExpiration() < System.currentTimeMillis()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "OTP code expired");
        }

        payment.setPaid(true);
        payment.setPaymentTime(LocalDateTime.now());
        paymentRepository.save(payment);

        Subscription subscription = payment.getSubscription();
        subscription.setActive(true);
        subscriptionRepository.save(subscription);
    }

    public PaymentResponse sendOtp(User user, String paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if (paymentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }

        Payment payment = paymentOptional.get();
        if (payment.isPaid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payment already paid");
        }

        payment.setOtpCode("123456");
        payment.setOtpExpiration(next30Minutes());
        paymentRepository.save(payment);

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .serviceMenuId(payment.getServiceMenu().getId())
                .subscriptionId(payment.getSubscription().getId())
                .status(payment.isPaid() ? "PAID" : "UNPAID")
                .otpCode(payment.getOtpCode())
                .otpExpiration(payment.getOtpExpiration())
                .message("OTP code has been sent to your email")
                .build();
    }


    public String status(User user, String paymentId) {
        Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
        if (paymentOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }

        Payment payment = paymentOptional.get();
        if (payment.isPaid()) {
            return "PAID";
        } else {
            return "UNPAID";
        }
    }

    private Long next30Minutes() {
        return System.currentTimeMillis() + 1000 * 60 * 30;
    }

}
