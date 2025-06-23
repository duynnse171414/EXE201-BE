package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.model.reponse.BlogResponse;
import com.example.swd391_be_hiv.model.request.BlogRequest;
import com.example.swd391_be_hiv.service.BlogService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@SecurityRequirement(name = "api")
@RestController
@RequestMapping("/api/blog")
public class BlogAPI {

    @Autowired
    BlogService blogService;

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(@Valid @RequestBody BlogRequest requestDTO) {
        BlogResponse response = blogService.createBlog(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("getBlogs")
    public ResponseEntity<List<BlogResponse>> getBlogs() {
        List<BlogResponse> blogList = blogService.getAllBlogs();
        return ResponseEntity.ok(blogList);
    }

    @GetMapping("{id}")
    public ResponseEntity<BlogResponse> getBlogById(@PathVariable Long id) {
        BlogResponse response = blogService.getBlogById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<BlogResponse> updateBlog(@Valid @RequestBody BlogRequest requestDTO, @PathVariable Long id) {
        BlogResponse response = blogService.updateBlog(requestDTO, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<BlogResponse> deleteBlog(@PathVariable Long id) {
        BlogResponse response = blogService.deleteBlog(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<BlogResponse>> getBlogsByStaff(@PathVariable Long staffId) {
        List<BlogResponse> blogs = blogService.getBlogsByStaff(staffId);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<BlogResponse>> getBlogsByTitle(@RequestParam String keyword) {
        List<BlogResponse> blogs = blogService.getBlogsByTitleContaining(keyword);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/search/content")
    public ResponseEntity<List<BlogResponse>> getBlogsByContent(@RequestParam String keyword) {
        List<BlogResponse> blogs = blogService.getBlogsByContentContaining(keyword);
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<BlogResponse>> getBlogsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<BlogResponse> blogs = blogService.getBlogsByDateRange(startDate, endDate);
        return ResponseEntity.ok(blogs);
    }
}