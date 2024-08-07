package com.oppa.project.payment_system.api.controller.product;

import com.oppa.project.payment_system.api.service.ProductService;
import com.oppa.project.payment_system.api.exception.ProductAlreadyExistsException;
import com.oppa.project.payment_system.api.model.entity.Product;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //all ar chirdeba
    @GetMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    //controller advice!!!
    //new ar chirdeba
    // ProductJSON maqvs dasamatebeli dto-ebshi
    @PostMapping("/new")
    public ResponseEntity createProduct(@Valid @RequestBody Product product) {
        try {
            productService.createProduct(product);
            return ResponseEntity.ok().build();
        } catch (ProductAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    //put unda ikos
    @PatchMapping("/{productId}")
    public ResponseEntity<Product> editProduct(@Valid @RequestBody Product product,
                                               @PathVariable Long productId) {
        if (product.getId() == productId) {
            Optional<Product> opOriginalProduct = productService.findById(productId);
            if (opOriginalProduct.isPresent()) {
                Product originalProduct = opOriginalProduct.get();
                if (product.getName() != null) {
                    originalProduct.setName(product.getName());
                }
                if (product.getMinValue() != null) {
                    originalProduct.setMinValue(product.getMinValue());
                }
                if (product.getMaxValue() != null) {
                    originalProduct.setMaxValue(product.getMaxValue());
                }
                Product updatedProduct = productService.updateProduct(originalProduct);
                return ResponseEntity.ok(updatedProduct);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity deleteProduct(@PathVariable Long productId) {
        Optional<Product> opOriginalProduct = productService.findById(productId);
        if (opOriginalProduct.isPresent()) {
            productService.deleteProduct(productId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}