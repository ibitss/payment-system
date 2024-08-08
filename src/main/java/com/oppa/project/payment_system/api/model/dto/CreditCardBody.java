package com.oppa.project.payment_system.api.model.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;

@Getter
@Setter
public class CreditCardBody {
    @NotNull(message = "Credit Card number is required")
    @NotBlank(message = "Credit Card number is required")
    @CreditCardNumber(message = "Credit card number is not valid")
    private String ccNumber;

    @NotNull(message = "Expiration date is required")
    @NotBlank(message = "Expiration date is required")
    @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$", message = "Must be formatted MM/YY")
    private String ccExpiration;

    @NotNull(message = "cvv is required")
    @NotBlank(message = "cvv is required")
    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    private String ccCVV;
}
