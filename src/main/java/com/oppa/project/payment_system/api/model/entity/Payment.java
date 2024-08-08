package com.oppa.project.payment_system.api.model.entity;

import com.oppa.project.payment_system.api.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private LocalUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_amount", nullable = false)
    private Double paymentAmount;

    @Column(name = "transaction_status", nullable = false)
    private String transactionStatus;


    @Column(name = "payment_date", nullable = false)
    private ZonedDateTime paymentDate;

    @Column(name = "cc_number", length = 30)
    private String ccNumber;

    @Column(name = "cc_expiration", length = 5)
    private String ccExpiration;

    @Column(name = "cc_cvv", length = 3)
    private String ccCVV;

}