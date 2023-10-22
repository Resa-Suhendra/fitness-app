package com.resa.fitness.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by Resa S.
 * Date: 22-10-2023
 * Created in IntelliJ IDEA.
 */
@Getter
@Setter
@Entity(name = "payments")
public class Payment {
    @Id
    private String id;

    private boolean paid;
    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "otp_expiration")
    private Long otpExpiration;
    private double amount;

    @Column(name = "payment_time")
    LocalDateTime paymentTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "service_menu_id")
    private ServiceMenu serviceMenu;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
}
