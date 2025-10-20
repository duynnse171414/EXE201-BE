package com.example.web_petvibe.api;

import com.example.web_petvibe.config.OrderMapper;
import com.example.web_petvibe.entity.Order;
import com.example.web_petvibe.model.request.OrderRequest;
import com.example.web_petvibe.model.request.UpdateOrderStatusRequest;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.OrderResponse;
import com.example.web_petvibe.service.OrderService;
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
    OrderMapper orderMapper;

    // Tạo đơn hàng - User đã đăng nhập
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Lấy danh sách đơn hàng theo user - Chỉ user đó hoặc admin
    @GetMapping("/orders/account/{id}")
    @PreAuthorize("hasRole('ADMIN') or @orderService.isOrderOwner(#id)")
    public ResponseEntity<List<OrderResponse>> getOrdersByAccountId(@PathVariable Long id) {
        List<Order> orders = orderService.getOrdersByAccount(id);
        List<OrderResponse> responses = orders.stream()
                .map(orderMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // Lấy chi tiết đơn hàng - Chỉ owner hoặc admin
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    // Cập nhật trạng thái đơn hàng - Chỉ Admin
    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status (Admin only)", description = "Valid statuses: PENDING, PAID, SHIPPED, DELIVERED, CANCELLED")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request) {
        Order order = orderService.updateOrderStatus(orderId, request.getStatus());
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    // Hủy đơn hàng - Owner hoặc Admin
    @PatchMapping("/{orderId}/cancel")
    @Operation(summary = "Cancel order")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId) {
        Order order = orderService.cancelOrder(orderId);
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    // Lấy tất cả đơn hàng - Chỉ Admin
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders (Admin only)")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponse> responses = orders.stream()
                .map(orderMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // Lấy đơn hàng theo status - Chỉ Admin
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get orders by status (Admin only)")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable String status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        List<OrderResponse> responses = orders.stream()
                .map(orderMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }
}