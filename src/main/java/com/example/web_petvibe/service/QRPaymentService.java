package com.example.web_petvibe.service;

import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class QRPaymentService {

    // Thông tin ngân hàng - Thay đổi theo thông tin thực tế của bạn
    private static final String BANK_ID = "970407"; // TECHCOMBANK
    private static final String ACCOUNT_NO = "19074497420010"; // Số tài khoản
    private static final String ACCOUNT_NAME = "NGUYEN TRAN GIA HUNG"; // Tên chủ tài khoản
    private static final String TEMPLATE = "compact2"; // Template QR

    /**
     * Tạo URL QR Code thanh toán VietQR
     * @param orderId ID đơn hàng
     * @param amount Số tiền thanh toán
     * @return URL của QR code
     */
    public String generatePaymentQR(Long orderId, Double amount) {
        try {
            // Nội dung chuyển khoản
            String description = "PETVIBE ORDER " + orderId;

            // Encode description để tránh lỗi với ký tự đặc biệt
            String encodedDescription = URLEncoder.encode(description, StandardCharsets.UTF_8.toString());

            // Tạo URL QR sử dụng VietQR API
            String qrUrl = String.format(
                    "https://img.vietqr.io/image/%s-%s-%s.png?amount=%s&addInfo=%s&accountName=%s",
                    BANK_ID,
                    ACCOUNT_NO,
                    TEMPLATE,
                    amount.intValue(), // Convert to integer
                    encodedDescription,
                    URLEncoder.encode(ACCOUNT_NAME, StandardCharsets.UTF_8.toString())
            );

            return qrUrl;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error generating QR code: " + e.getMessage());
        }
    }

    /**
     * Tạo thông tin thanh toán chi tiết
     * @param orderId ID đơn hàng
     * @param amount Số tiền
     * @return PaymentInfo object
     */
    public PaymentInfo generatePaymentInfo(Long orderId, Double amount) {
        String qrUrl = generatePaymentQR(orderId, amount);
        String description = "PETVIBE ORDER " + orderId;

        return PaymentInfo.builder()
                .qrCodeUrl(qrUrl)
                .bankId(BANK_ID)
                .accountNo(ACCOUNT_NO)
                .accountName(ACCOUNT_NAME)
                .amount(amount)
                .description(description)
                .orderId(orderId)
                .status("PENDING") // Trạng thái mặc định
                .build();
    }

    // Inner class để chứa thông tin thanh toán
    @lombok.Data
    @lombok.Builder
    public static class PaymentInfo {
        private String qrCodeUrl;
        private String bankId;
        private String accountNo;
        private String accountName;
        private Double amount;
        private String description;
        private Long orderId;

        @lombok.Builder.Default
        private String status = "PENDING"; // Trạng thái thanh toán: PENDING, COMPLETED, FAILED, EXPIRED
    }
}