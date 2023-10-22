package com.resa.fitness.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Resa S.
 * Date: 22-10-2023
 * Created in IntelliJ IDEA.
 */
@Data
@Getter
@Setter
public class RequestPayments {
    private String paymentId;
    private Long subscriptionId;
    private Long serviceMenuId;
    private String otpCode;
}