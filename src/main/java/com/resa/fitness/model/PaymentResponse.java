package com.resa.fitness.model;

import lombok.*;

/**
 * Created by Resa S.
 * Date: 22-10-2023
 * Created in IntelliJ IDEA.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String paymentId;
    private Long subscriptionId;
    private Long serviceMenuId;
    private Long otpExpiration;
    private double amount;
    private String otpCode;
    private String status;
    private String message;
}
