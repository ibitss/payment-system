package com.oppa.project.payment_system.api.controller.product;

import com.oppa.project.payment_system.api.model.dto.ProductBody;
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

    @PostMapping("/new")
    public ResponseEntity createProduct(@Valid @RequestBody ProductBody productBody) {
        try {
            productService.createProduct(productBody);
            return ResponseEntity.ok().build();
        } catch (ProductAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductBody> editProduct(@Valid @RequestBody ProductBody productBody,
                                               @PathVariable Long productId) {
        if (productBody.getId() == productId) {
            Optional<Product> opOriginalProduct = productService.findById(productId);
            if (opOriginalProduct.isPresent()) {
                Product originalProduct = opOriginalProduct.get();
                if (productBody.getName() != null) {
                    originalProduct.setName(productBody.getName());
                }
                if (productBody.getMinValue() != null) {
                    originalProduct.setMinValue(productBody.getMinValue());
                }
                if (productBody.getMaxValue() != null) {
                    originalProduct.setMaxValue(productBody.getMaxValue());
                }
                Product updatedProduct = productService.updateProduct(originalProduct);
                ProductBody updatedProductBody = new ProductBody();
                updatedProductBody.setId(updatedProduct.getId());
                updatedProductBody.setName(updatedProduct.getName());
                updatedProductBody.setMinValue(updatedProduct.getMinValue());
                updatedProductBody.setMaxValue(updatedProduct.getMaxValue());
                return ResponseEntity.ok(productBody);
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