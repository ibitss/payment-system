package com.oppa.project.payment_system.api.controller.payment;

import com.oppa.project.payment_system.api.model.dto.PaymentBody;
import com.oppa.project.payment_system.api.service.PaymentService;
import com.oppa.project.payment_system.api.service.ProductService;
import com.oppa.project.payment_system.api.model.entity.LocalUser;
import com.oppa.project.payment_system.api.model.entity.Payment;
import com.oppa.project.payment_system.api.enums.PaymentMethod;
import com.oppa.project.payment_system.api.model.entity.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping("/new")
    public ResponseEntity processPayment(@AuthenticationPrincipal LocalUser user,
                                         @Valid @RequestBody PaymentBody paymentBody) {
        Optional<Product> opProduct = productService.findById(paymentBody.getProductId());
        if (opProduct.isPresent()) {
            if (paymentService.isProductAmountValid(paymentBody.getProductId(), paymentBody.getAmount())) {
                paymentService.processPayment(user, opProduct.get(), paymentBody);
                if (paymentBody.getPaymentMethod() == PaymentMethod.CARD) {
                    if (paymentBody.getCcNumber() == null ||
                            paymentBody.getCcExpiration() == null ||
                            paymentBody.getCcCVV() == null) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credit card details are missing");
                    }
                    if (paymentService.isCCExpired(paymentBody.getCcExpiration())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Expiration Date");
                    }
                }
            } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid amount");
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid product ID");
    }
}
