package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.Category;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.CategoryResponse;
import com.example.web_petvibe.service.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
@SecurityRequirement(name = "api")
public class CategoryAPI {

    @Autowired
    private CategoryService categoryService;

    // GET all categories
    @GetMapping("/getAll")
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            return ResponseEntity.ok(categoryService.getAllCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET category by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        return category.isPresent()
                ? ResponseEntity.ok(new CategoryResponse("Category found successfully", true, category.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category not found with id: " + id, false));
    }

    // POST create category
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        try {
            Category created = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(new CategoryResponse("Category created successfully", true, created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
        }
    }

    // DELETE category
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("Category deleted successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }
}
