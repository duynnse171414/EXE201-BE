package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.Category;
import com.example.web_petvibe.entity.Product;
import com.example.web_petvibe.repository.CategoryRepository;
import com.example.web_petvibe.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAllActive();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findByIdActive(id);
    }

    public Product createProduct(Product product, Long categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (!categoryOpt.isPresent()) {
            throw new RuntimeException("Category not found with id: " + categoryId);
        }

        Category category = categoryOpt.get();

        // Kiểm tra Category đã bị xóa chưa
        if (category.isDeleted()) {
            throw new RuntimeException("Cannot create product. Category with id: "
                    + categoryId + " has been deleted.");
        }

        product.setCategory(category);
        product.setDeleted(false);
        return productRepository.save(product);
    }


    public Product updateProduct(Long id, Product updatedProduct, Long categoryId) {
        Optional<Product> productOpt = productRepository.findByIdActive(id);

        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        Product product = productOpt.get();

        // Nếu truyền categoryId mới thì kiểm tra
        if (categoryId != null) {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
            if (!categoryOpt.isPresent()) {
                throw new RuntimeException("Category not found with id: " + categoryId);
            }
            Category category = categoryOpt.get();
            if (category.isDeleted()) {
                throw new RuntimeException("Cannot update product. Category with id: "
                        + categoryId + " has been deleted.");
            }
            product.setCategory(category);
        }

        // Cập nhật các field
        if (updatedProduct.getName() != null) {
            product.setName(updatedProduct.getName());
        }
        if (updatedProduct.getDescription() != null) {
            product.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getPrice() != null) {
            product.setPrice(updatedProduct.getPrice());
        }
        if (updatedProduct.getStock() != null) {
            product.setStock(updatedProduct.getStock());
        }

        return productRepository.save(product);
    }




    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findByIdActive(id);
        if (product.isPresent()) {
            Product p = product.get();
            p.setDeleted(true);
            productRepository.save(p);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}