package com.oppa.project.payment_system.api.model.repository;


import com.oppa.project.payment_system.api.model.entity.Product;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ProductDAO extends ListCrudRepository<Product, Long> {

    Optional<Product> findByNameIgnoreCase(String productName);

}
