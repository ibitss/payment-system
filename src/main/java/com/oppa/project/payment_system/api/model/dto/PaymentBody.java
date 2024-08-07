package com.oppa.project.payment_system.api.model.dto;

import com.oppa.project.payment_system.api.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.Getter;
import org.hibernate.validator.constraints.CreditCardNumber;

@Getter
public class PaymentBody {

    @NotNull(message = "Product Id is required")
    private Long productId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount input should be a positive number")
    private Double amount;

    @NotNull(message = "Payment method is required: choose CASH or CARD")
    private PaymentMethod paymentMethod;

    @CreditCardNumber(message = "Credit card number is not valid")
    private String ccNumber;

    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message = "Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;

}
