package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.Category;
import com.example.web_petvibe.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    // Lấy tất cả category
    public List<Category> getAllCategories() {
        return categoryRepository.findAllActive();
    }

    // Lấy category theo ID
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findByIdActive(id);
    }

    // Tạo mới category
    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with name '" + category.getName() + "' already exists");
        }
        category.setDeleted(false);
        return categoryRepository.save(category);
    }
    // Cập nhật category
    public Category updateCategory(Long id, Category categoryDetails) {
        Optional<Category> existingCategory = categoryRepository.findByIdActive(id);

        if (!existingCategory.isPresent()) {
            throw new RuntimeException("Category not found with id: " + id);
        }

        Category category = existingCategory.get();

        // Kiểm tra trùng tên (nếu tên mới khác tên cũ)
        if (!category.getName().equals(categoryDetails.getName())
                && categoryRepository.existsByName(categoryDetails.getName())) {
            throw new RuntimeException("Category with name '" + categoryDetails.getName() + "' already exists");
        }

        // Cập nhật thông tin
        category.setName(categoryDetails.getName());

        // Cập nhật description nếu có
        if (categoryDetails.getDescription() != null) {
            category.setDescription(categoryDetails.getDescription());
        }

        return categoryRepository.save(category);
    }

    // Xóa mềm category
    public void deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findByIdActive(id);
        if (category.isPresent()) {
            Category c = category.get();
            c.setDeleted(true);
            categoryRepository.save(c);
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }
}
