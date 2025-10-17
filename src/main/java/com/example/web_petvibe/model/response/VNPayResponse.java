package com.example.web_petvibe.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VNPayResponse {
    private String code; // Mã response (00 = success)
    private String message;
    private String paymentUrl; // URL để redirect user đến trang thanh toán
}