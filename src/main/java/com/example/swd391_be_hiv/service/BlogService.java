package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.Blog;
import com.example.swd391_be_hiv.entity.Staff;
import com.example.swd391_be_hiv.exception.DuplicateEntity;
import com.example.swd391_be_hiv.exception.NotFoundException;
import com.example.swd391_be_hiv.model.reponse.BlogResponse;
import com.example.swd391_be_hiv.model.request.BlogRequest;
import com.example.swd391_be_hiv.repository.BlogRepository;
import com.example.swd391_be_hiv.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    StaffRepository staffRepository;

    public BlogResponse createBlog(BlogRequest requestDTO) {
        try {

            Staff staff = staffRepository.findById(requestDTO.getStaffId())
                    .orElseThrow(() -> new NotFoundException("Staff not found"));


            Blog blog = new Blog();
            blog.setTitle(requestDTO.getTitle());
            blog.setStaff(staff);
            blog.setContent(requestDTO.getContent());
            blog.setCreateDate(LocalDateTime.now());

            Blog savedBlog = blogRepository.save(blog);
            return convertToResponseDTO(savedBlog);

        } catch (Exception e) {
            throw new DuplicateEntity("Error creating blog: " + e.getMessage());
        }
    }

    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public BlogResponse updateBlog(BlogRequest requestDTO, Long blogId) {
        Blog existingBlog = blogRepository.findBlogByBlogId(blogId);
        if (existingBlog == null) {
            throw new NotFoundException("Blog not found");
        }


        Staff staff = staffRepository.findById(requestDTO.getStaffId())
                .orElseThrow(() -> new NotFoundException("Staff not found"));


        existingBlog.setTitle(requestDTO.getTitle());
        existingBlog.setStaff(staff);
        existingBlog.setContent(requestDTO.getContent());


        Blog updatedBlog = blogRepository.save(existingBlog);
        return convertToResponseDTO(updatedBlog);
    }

    public BlogResponse deleteBlog(Long blogId) {
        Blog blog = blogRepository.findBlogByBlogId(blogId);
        if (blog == null) {
            throw new NotFoundException("Blog not found");
        }

        BlogResponse response = convertToResponseDTO(blog);
        blogRepository.delete(blog);
        return response;
    }

    public List<BlogResponse> getBlogsByStaff(Long staffId) {
        return blogRepository.findBlogsByStaff_StaffId(staffId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BlogResponse> getBlogsByTitleContaining(String keyword) {
        return blogRepository.findBlogsByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BlogResponse> getBlogsByContentContaining(String keyword) {
        return blogRepository.findBlogsByContentContaining(keyword)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<BlogResponse> getBlogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return blogRepository.findBlogsByCreateDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public BlogResponse getBlogById(Long blogId) {
        Blog blog = blogRepository.findBlogByBlogId(blogId);
        if (blog == null) {
            throw new NotFoundException("Blog not found");
        }
        return convertToResponseDTO(blog);
    }

    private BlogResponse convertToResponseDTO(Blog blog) {
        BlogResponse dto = new BlogResponse();
        dto.setBlogId(blog.getBlogId());
        dto.setTitle(blog.getTitle());
        dto.setStaffId(blog.getStaff().getStaffId());
        dto.setStaffName(blog.getStaff().getName());
        dto.setContent(blog.getContent());
        dto.setCreateDate(blog.getCreateDate());

        return dto;
    }
}