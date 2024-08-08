package com.oppa.project.payment_system.api.controller.payment;

import com.oppa.project.payment_system.api.exception.PaymentProcessingException;
import com.oppa.project.payment_system.api.model.dto.PaymentBody;
import com.oppa.project.payment_system.api.service.PaymentService;
import com.oppa.project.payment_system.api.service.ProductService;
import com.oppa.project.payment_system.api.model.entity.LocalUser;
import com.oppa.project.payment_system.api.model.entity.Payment;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private PaymentService paymentService;
    private ProductService productService;

    public PaymentController(PaymentService paymentService, ProductService productService) {
        this.paymentService = paymentService;
        this.productService = productService;
    }

    @GetMapping
    public List<Payment> getPayments(@AuthenticationPrincipal LocalUser user) {
        return paymentService.getPaymentHistory(user);
    }

    @PostMapping
    public ResponseEntity processPayment(@AuthenticationPrincipal LocalUser user,
                                         @Valid @RequestBody PaymentBody paymentBody, BindingResult result) {
        if (result.hasErrors()) {
            // Capture validation errors
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            paymentService.processPayment(user, paymentBody);
            return ResponseEntity.ok().build();
        } catch (PaymentProcessingException ex) {
            return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
        }
    }

}
