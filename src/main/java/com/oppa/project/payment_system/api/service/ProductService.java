package com.oppa.project.payment_system.api.service;

import com.oppa.project.payment_system.api.exception.ProductAlreadyExistsException;
import com.oppa.project.payment_system.api.model.dto.ProductBody;
import com.oppa.project.payment_system.api.model.entity.Product;
import com.oppa.project.payment_system.api.model.repository.ProductDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> getProducts() {
        return productDAO.findAll();
    }

    public Product createProduct(ProductBody productBody) throws ProductAlreadyExistsException {
        Optional<Product> opProduct = productDAO.findByNameIgnoreCase(productBody.getName());
        if (opProduct.isPresent()) {
            throw new ProductAlreadyExistsException();
        }
        Product newProduct = new Product();
        newProduct.setName(productBody.getName());
        newProduct.setMinValue(productBody.getMaxValue());
        newProduct.setMaxValue(productBody.getMaxValue());
        return productDAO.save(newProduct);
    }

    public Product updateProduct(Product product) {
        return productDAO.save(product);
    }

    public Optional<Product> findById(Long productId) {
        return productDAO.findById(productId);
    }

    public void deleteProduct(Long productId) {
        productDAO.deleteById(productId);
    }

}
