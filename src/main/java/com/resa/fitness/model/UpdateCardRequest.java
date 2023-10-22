package com.resa.fitness.model;

import lombok.*;

/**
 * Created by Resa S.
 * Date: 21-10-2023
 * Created in IntelliJ IDEA.
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UpdateCardRequest {
    private String cardName;
    private String cardNumber;
    private String cardCvv;
    private String cardExpired;
}
