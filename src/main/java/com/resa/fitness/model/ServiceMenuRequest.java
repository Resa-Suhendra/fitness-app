package com.resa.fitness.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by Resa S.
 * Date: 21-10-2023
 * Created in IntelliJ IDEA.
 */
@Data
@Getter
@Setter
public class ServiceMenuRequest {
    private String name;
    private String description;
    private double pricePerSession;
    private int totalSessions;
    private int durationInMinutes;
    private String schedule;
}
