package com.oppa.project.payment_system.api.service;


import com.oppa.project.payment_system.api.model.dto.PaymentBody;
import com.oppa.project.payment_system.api.model.entity.LocalUser;
import com.oppa.project.payment_system.api.model.entity.Payment;
import com.oppa.project.payment_system.api.enums.PaymentMethod;
import com.oppa.project.payment_system.api.model.entity.Product;
import com.oppa.project.payment_system.api.model.repository.PaymentDAO;
import com.oppa.project.payment_system.api.model.repository.ProductDAO;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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


    public void processPayment(LocalUser user, Product product, PaymentBody paymentBody) {
            Payment potentialPayment = new Payment();
            potentialPayment.setProduct(product);
            potentialPayment.setUser(user);
            potentialPayment.setPaymentAmount(paymentBody.getAmount());
            potentialPayment.setPaymentMethod(paymentBody.getPaymentMethod());
            potentialPayment.setPaymentDate(ZonedDateTime.now());
            potentialPayment.setTransactionStatus("SUCCESS");
            if(potentialPayment.getPaymentMethod() == PaymentMethod.CARD) {
                potentialPayment.setCcNumber(paymentBody.getCcNumber());
                potentialPayment.setCcExpiration(paymentBody.getCcExpiration());
                potentialPayment.setCcCVV(paymentBody.getCcCVV());
                if (isCCExpired(paymentBody.getCcExpiration())) {
                    potentialPayment.setTransactionStatus("FAILURE");
                }
            }
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


}
