package com.oppa.project.payment_system.api.model.dto;

import com.oppa.project.payment_system.api.enums.PaymentMethod;
import jakarta.validation.Valid;
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

    @Valid
    private CreditCardBody creditCardBody;

}
