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
    private LocalDateTime createdAt;
    private String accountName;
    private List<OrderItemResponse> items;

    @Getter
    @Setter
    public static class OrderItemResponse {
        private Long productId;
        private String productName;
        private Integer quantity;
        private Double price;
    }
}
