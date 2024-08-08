package com.oppa.project.payment_system.api.service;


import com.oppa.project.payment_system.api.exception.PaymentProcessingException;
import com.oppa.project.payment_system.api.model.dto.CreditCardBody;
import com.oppa.project.payment_system.api.model.dto.PaymentBody;
import com.oppa.project.payment_system.api.model.entity.LocalUser;
import com.oppa.project.payment_system.api.model.entity.Payment;
import com.oppa.project.payment_system.api.enums.PaymentMethod;
import com.oppa.project.payment_system.api.model.entity.Product;
import com.oppa.project.payment_system.api.model.repository.PaymentDAO;
import com.oppa.project.payment_system.api.model.repository.ProductDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class PaymentService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/yy");
    private PaymentDAO paymentDAO;
    private ProductDAO productDAO;

    public PaymentService(PaymentDAO paymentDAO, ProductDAO productDAO) {
        this.paymentDAO = paymentDAO;
        this.productDAO = productDAO;
    }

    public List<Payment> getPaymentHistory(LocalUser user) {
        return paymentDAO.findPaymentByUserId(user.getId());
    }


    public void processPayment(LocalUser user, PaymentBody paymentBody) {
        log.info("Processing of a new payment started");
        Optional<Product> opProduct = productDAO.findById(paymentBody.getProductId());
        Payment potentialPayment = createBasePayment(user, paymentBody);
        log.info("Validating productId " + paymentBody.getProductId().toString());
        String message;
        if(opProduct.isEmpty()) {
            message ="Product not found";
            log.info(message);
            processFailedPayment(potentialPayment,"FAILURE", message);
            return;
        }
        log.info("products is valid");
        potentialPayment.setProduct(opProduct.get());
        if (!isProductAmountValid(paymentBody.getProductId(), paymentBody.getAmount())) {
            message="Amount " + paymentBody.getAmount() + " is not valid";
            log.info(message);
            processFailedPayment(potentialPayment, "FAILURE", opProduct.get(), message);
            return;
        }
        log.info("Amount is valid");
        if (paymentBody.getPaymentMethod() == PaymentMethod.CARD)  {
            log.info("Processing credit card details");
            if (paymentBody.getCreditCardBody() == null || isCCExpired(paymentBody.getCreditCardBody().getCcExpiration())) {
                message = "Valid credit card details required";
                processFailedPayment(potentialPayment, "FAILURE", opProduct.get(), message);
                return;
            }
            log.info("Credit card details are valid");
            setCreditCardDetails(potentialPayment, paymentBody.getCreditCardBody());
        }
        log.info("Payment is successful");
        potentialPayment.setTransactionStatus("SUCCESS");
        paymentDAO.save(potentialPayment);

    }


    public boolean isCCExpired(String expirationDate) {
        YearMonth yearMonthExp = YearMonth.parse(expirationDate, DATE_FORMATTER);
        YearMonth currentDate = YearMonth.now();
        return yearMonthExp.isBefore(currentDate);
    }

    public boolean isProductAmountValid(Long productId, Double amount) {
        Product product = productDAO.findById(productId).get();
        return product.getMinValue() <= amount && amount <= product.getMaxValue();
    }

    private Payment createBasePayment(LocalUser user, PaymentBody paymentBody) {
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentAmount(paymentBody.getAmount());
        payment.setPaymentMethod(paymentBody.getPaymentMethod());
        payment.setPaymentDate(ZonedDateTime.now());
        if (payment.getPaymentMethod() == PaymentMethod.CARD && paymentBody.getCreditCardBody() != null) {
            setCreditCardDetails(payment, paymentBody.getCreditCardBody());
        }
        return payment;
    }
    private void processFailedPayment(Payment payment, String status, String errorMessage) {
        payment.setTransactionStatus(status);
        paymentDAO.save(payment);
        throw new PaymentProcessingException(HttpStatus.NOT_FOUND, errorMessage);
    }

    private void processFailedPayment(Payment payment, String status, Product product, String errorMessage) {
        payment.setProduct(product);
        payment.setTransactionStatus(status);
        paymentDAO.save(payment);
        throw new PaymentProcessingException(HttpStatus.BAD_REQUEST, errorMessage);
    }
    private void setCreditCardDetails(Payment payment, CreditCardBody creditCardBody) {
        payment.setCcNumber(creditCardBody.getCcNumber());
        payment.setCcExpiration(creditCardBody.getCcExpiration());
        payment.setCcCVV(creditCardBody.getCcCVV());
    }



}
