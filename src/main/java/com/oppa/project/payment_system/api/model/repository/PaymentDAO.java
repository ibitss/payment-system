package com.oppa.project.payment_system.api.model.repository;

import com.oppa.project.payment_system.api.model.entity.Payment;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;


public interface PaymentDAO extends ListCrudRepository<Payment, Long> {
    List<Payment> findPaymentByUserId(Long userId);

}
