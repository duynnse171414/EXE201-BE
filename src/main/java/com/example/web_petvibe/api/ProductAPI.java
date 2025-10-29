package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.Product;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.CreateProductResponse;
import com.example.web_petvibe.model.response.GetProductResponse;
import com.example.web_petvibe.model.request.ProductRequest;
import com.example.web_petvibe.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@SecurityRequirement(name = "api")
public class ProductAPI {

    @Autowired
    private ProductService productService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            return ResponseEntity.ok(productService.getAllProducts());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("getProductId/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            Product p = product.get();
            return ResponseEntity.ok(
                    new GetProductResponse(
                            "Product found successfully",
                            true,
                            p.getId(),
                            p.getName(),
                            p.getDescription(),
                            p.getPrice(),
                            p.getImageUrl(),
                            p.getType(),
                            p.getStock(),
                            p.getCategory()
                    )
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Product not found", false));
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest request) {
        try {
            Product product = new Product();
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setStock(request.getStock());
            product.setImageUrl(request.getImageUrl());
            product.setType(request.getType());

            Product created = productService.createProduct(product, request.getCategoryId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CreateProductResponse("Product created successfully", true, created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @Valid @RequestBody ProductRequest request) {
        try {
            Product updatedProduct = new Product();
            updatedProduct.setName(request.getName());
            updatedProduct.setDescription(request.getDescription());
            updatedProduct.setPrice(request.getPrice());
            updatedProduct.setStock(request.getStock());
            updatedProduct.setType(request.getType());
            updatedProduct.setImageUrl(request.getImageUrl());

            Product result = productService.updateProduct(id, updatedProduct, request.getCategoryId());
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }
}