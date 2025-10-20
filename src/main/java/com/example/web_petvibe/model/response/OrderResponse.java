package com.example.web_petvibe.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponse {
    private Long orderId;
    private String shippingAddress;
    private String phoneContact;
    private Double totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String accountName;
    private String note;
    private List<OrderItemResponse> items;

    // Thông tin thanh toán QR
    private PaymentInfoResponse paymentInfo;

    @Getter
    @Setter
    public static class OrderItemResponse {
        private Long productId;
        private String productName;
        private Integer quantity;
        private Double price;
    }

    @Getter
    @Setter
    public static class PaymentInfoResponse {
        private String qrCodeUrl;
        private String bankId;
        private String accountNo;
        private String accountName;
        private Double amount;
        private String description;
        private String message; // Hướng dẫn thanh toán
    }
}