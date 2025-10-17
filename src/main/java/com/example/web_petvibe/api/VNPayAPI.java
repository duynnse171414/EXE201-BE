package com.example.web_petvibe.api;

import com.example.web_petvibe.model.request.VNPayRequest;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.VNPayResponse;
import com.example.web_petvibe.service.VNPayService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment/vnpay")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class VNPayAPI {

    private final VNPayService vnPayService;

    /**
     * Tạo URL thanh toán VNPAY
     * POST /api/payment/vnpay/create
     */
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody VNPayRequest request, HttpServletRequest httpRequest) {
        try {
            String paymentUrl = vnPayService.createPaymentUrl(request, httpRequest);
            VNPayResponse response = new VNPayResponse("00", "Success", paymentUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Error creating payment: " + e.getMessage(), false));
        }
    }

    /**
     * Callback từ VNPAY sau khi thanh toán
     * GET /api/payment/vnpay/callback
     */
    @GetMapping("/callback")
    public ResponseEntity<?> paymentCallback(@RequestParam Map<String, String> params) {
        try {
            // Validate signature
            boolean isValid = vnPayService.validateCallback(params);

            if (!isValid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("Invalid signature", false));
            }

            String responseCode = params.get("vnp_ResponseCode");
            String transactionStatus = vnPayService.getTransactionStatus(responseCode);

            Map<String, Object> result = new HashMap<>();
            result.put("success", "00".equals(responseCode));
            result.put("responseCode", responseCode);
            result.put("message", transactionStatus);
            result.put("transactionNo", params.get("vnp_TransactionNo"));
            result.put("amount", params.get("vnp_Amount"));
            result.put("orderInfo", params.get("vnp_OrderInfo"));
            result.put("bankCode", params.get("vnp_BankCode"));
            result.put("payDate", params.get("vnp_PayDate"));

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error processing callback: " + e.getMessage(), false));
        }
    }

    /**
     * IPN (Instant Payment Notification) từ VNPAY
     * GET /api/payment/vnpay/ipn
     */
    @GetMapping("/ipn")
    public ResponseEntity<?> paymentIPN(@RequestParam Map<String, String> params) {
        try {
            boolean isValid = vnPayService.validateCallback(params);

            if (!isValid) {
                Map<String, String> response = new HashMap<>();
                response.put("RspCode", "97");
                response.put("Message", "Invalid Signature");
                return ResponseEntity.ok(response);
            }

            String responseCode = params.get("vnp_ResponseCode");

            // TODO: Cập nhật trạng thái đơn hàng trong database
            // String orderId = params.get("vnp_TxnRef");
            // Update order status based on responseCode

            Map<String, String> response = new HashMap<>();
            response.put("RspCode", "00");
            response.put("Message", "Confirm Success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("RspCode", "99");
            response.put("Message", "Unknown error");
            return ResponseEntity.ok(response);
        }
    }
}