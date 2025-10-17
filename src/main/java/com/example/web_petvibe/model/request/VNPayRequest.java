package com.example.web_petvibe.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VNPayRequest {
    private Long orderId;
    private Long amount; // Số tiền (VNĐ)
    private String orderInfo; // Thông tin đơn hàng
    private String returnUrl; // URL return sau khi thanh toán (optional)
}