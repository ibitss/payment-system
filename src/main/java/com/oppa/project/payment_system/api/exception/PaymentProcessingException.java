package com.oppa.project.payment_system.api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class PaymentProcessingException extends RuntimeException{
    private HttpStatus status;

    public PaymentProcessingException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
