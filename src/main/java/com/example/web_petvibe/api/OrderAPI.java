package com.example.web_petvibe.api;

import com.example.web_petvibe.config.OrderMapper;
import com.example.web_petvibe.entity.Order;
import com.example.web_petvibe.model.request.OrderRequest;
import com.example.web_petvibe.model.request.UpdateOrderStatusRequest;
import com.example.web_petvibe.model.request.UpdatePaymentStatusRequest;
import com.example.web_petvibe.model.response.OrderResponse;
import com.example.web_petvibe.service.OrderService;
import com.example.web_petvibe.service.QRPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@SecurityRequirement(name = "api")
public class OrderAPI {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private QRPaymentService qrPaymentService;

    // Tạo đơn hàng - Trả về kèm QR code thanh toán
    @PostMapping
    @Operation(summary = "Create new order with payment QR code")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        // Tạo order
        Order order = orderService.createOrder(request);

        // Tạo QR payment info (chỉ khi order PENDING)
        QRPaymentService.PaymentInfo paymentInfo = null;
        if ("PENDING".equals(order.getStatus())) {
            paymentInfo = qrPaymentService.generatePaymentInfo(order.getId(), order.getTotalAmount());
        }

        // Map sang response với payment info
        OrderResponse response = orderMapper.toResponseWithPayment(order, paymentInfo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Lấy QR code thanh toán cho order đã tạo
    @GetMapping("/{orderId}/payment-qr")
    @Operation(summary = "Get payment QR code for existing order")
    public ResponseEntity<?> getPaymentQR(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);

        // Chỉ có thể lấy QR khi order đang PENDING
        if (!"PENDING".equals(order.getStatus())) {
            return ResponseEntity.badRequest()
                    .body("Cannot generate QR code. Order status is: " + order.getStatus());
        }

        QRPaymentService.PaymentInfo paymentInfo =
                qrPaymentService.generatePaymentInfo(order.getId(), order.getTotalAmount());

        return ResponseEntity.ok(paymentInfo);
    }

    // API MỚI: Cập nhật trạng thái thanh toán
    @PatchMapping("/{orderId}/payment-status")
    @Operation(summary = "Update payment status for order")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestBody UpdatePaymentStatusRequest request) {

        Order order = orderService.getOrderById(orderId);

        // Validate payment status
        String paymentStatus = request.getPaymentStatus().toUpperCase();
        if (!List.of("PENDING", "COMPLETED", "FAILED", "EXPIRED").contains(paymentStatus)) {
            return ResponseEntity.badRequest()
                    .body("Invalid payment status. Must be: PENDING, COMPLETED, FAILED, or EXPIRED");
        }

        // Tạo PaymentInfo với status mới
        QRPaymentService.PaymentInfo paymentInfo = qrPaymentService.generatePaymentInfo(
                order.getId(),
                order.getTotalAmount()
        );

        // Override status
        paymentInfo.setStatus(paymentStatus);

        // Nếu payment COMPLETED, tự động cập nhật order status thành CONFIRMED
        if ("COMPLETED".equals(paymentStatus) && "PENDING".equals(order.getStatus())) {
            order = orderService.updateOrderStatus(orderId, "CONFIRMED");
        }

        // Map response
        OrderResponse response = orderMapper.toResponseWithPayment(order, paymentInfo);

        return ResponseEntity.ok(response);
    }

    // API MỚI: Xác nhận thanh toán thành công (shortcut)
    @PostMapping("/{orderId}/confirm-payment")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(summary = "Confirm payment completed (Admin/Staff only)")
    public ResponseEntity<?> confirmPayment(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!"PENDING".equals(order.getStatus())) {
            return ResponseEntity.badRequest()
                    .body("Order is not in PENDING status. Current status: " + order.getStatus());
        }

        // Tạo PaymentInfo với COMPLETED status
        QRPaymentService.PaymentInfo paymentInfo = qrPaymentService.generatePaymentInfo(
                order.getId(),
                order.getTotalAmount()
        );
        paymentInfo.setStatus("COMPLETED");

        // Tự động cập nhật order status
        order = orderService.updateOrderStatus(orderId, "PAID");

        OrderResponse response = orderMapper.toResponseWithPayment(order, paymentInfo);

        return ResponseEntity.ok(response);
    }

    // Lấy danh sách đơn hàng theo user
    @GetMapping("/account/{id}")
    @Operation(summary = "Get orders by account ID")
    public ResponseEntity<List<OrderResponse>> getOrdersByAccountId(@PathVariable Long id) {
        List<Order> orders = orderService.getOrdersByAccount(id);
        List<OrderResponse> responses = orders.stream()
                .map(orderMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // Lấy chi tiết đơn hàng
    @GetMapping("/{orderId}")
    @Operation(summary = "Get order details by ID")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    // Cập nhật trạng thái đơn hàng - Chỉ Admin/Staff
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(summary = "Update order status (Admin/Staff only)")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request) {
        Order order = orderService.updateOrderStatus(orderId, request.getStatus());
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    // Hủy đơn hàng
    @PatchMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {
        Order order = orderService.cancelOrder(orderId);
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    // Lấy tất cả đơn hàng - Chỉ Admin/Staff
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(summary = "Get all orders (Admin/Staff only)")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponse> responses = orders.stream()
                .map(orderMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // Lấy đơn hàng theo status - Chỉ Admin/Staff
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    @Operation(summary = "Get orders by status (Admin/Staff only)")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable String status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        List<OrderResponse> responses = orders.stream()
                .map(orderMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
}