package com.example.web_petvibe.api;

import com.example.web_petvibe.config.OrderMapper;
import com.example.web_petvibe.entity.Order;
import com.example.web_petvibe.model.request.OrderRequest;
import com.example.web_petvibe.model.response.OrderResponse;
import com.example.web_petvibe.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Tạo đơn hàng
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // Lấy danh sách đơn hàng theo user
    @GetMapping("/orders/account/{id}")
    public ResponseEntity<List<OrderResponse>> getOrdersByAccountId(@PathVariable Long id) {
        List<Order> orders = orderService.getOrdersByAccount(id);
        List<OrderResponse> responses = orders.stream()
                .map(orderMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }


    // Lấy chi tiết đơn hàng
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        OrderResponse response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

}
