package com.example.web_petvibe.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Account đặt đơn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Danh sách OrderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderItems;

    private Double totalAmount;

    private String status; // PENDING, PAID, SHIPPED, DELIVERED, CANCELLED

    private String shippingAddress;

    private String phoneContact;
    @CreationTimestamp // Hibernate tự động set khi insert
    private LocalDateTime createdAt;
    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.orderDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}