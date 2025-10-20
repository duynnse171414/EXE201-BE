package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.*;
import com.example.web_petvibe.exception.NotFoundException;
import com.example.web_petvibe.model.response.OrderResponse;
import com.example.web_petvibe.model.request.OrderRequest;
import com.example.web_petvibe.repository.AccountRepository;
import com.example.web_petvibe.repository.OrderRepository;
import com.example.web_petvibe.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductRepository productRepository;

    // Danh sách các trạng thái hợp lệ
    private static final List<String> VALID_STATUSES = Arrays.asList(
            "PENDING", "PAID", "SHIPPED", "DELIVERED", "CANCELLED"
    );

    // Danh sách chuyển đổi trạng thái hợp lệ
    private static final java.util.Map<String, List<String>> VALID_TRANSITIONS = java.util.Map.of(
            "PENDING", Arrays.asList("PAID", "CANCELLED"),
            "PAID", Arrays.asList("SHIPPED", "CANCELLED"),
            "SHIPPED", Arrays.asList("DELIVERED", "CANCELLED"),
            "DELIVERED", Arrays.asList(), // Không thể chuyển sang trạng thái khác
            "CANCELLED", Arrays.asList()  // Không thể chuyển sang trạng thái khác
    );

    // Tạo đơn hàng
    public Order createOrder(OrderRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        Order order = new Order();
        order.setAccount(account);
        order.setShippingAddress(request.getShippingAddress());
        order.setPhoneContact(request.getPhoneContact());
        order.setNote(request.getNote());

        List<OrderDetails> items = new ArrayList<>();
        double total = 0;

        for (OrderRequest.OrderDetailsRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));

            // Kiểm tra Product đã bị xóa chưa
            if (product.isDeleted()) {
                throw new RuntimeException("Cannot create product. Product with id: "
                        + product.getId() + " has been deleted.");
            }

            OrderDetails orderItem = new OrderDetails();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPrice(product.getPrice() * itemReq.getQuantity());

            items.add(orderItem);
            total += orderItem.getPrice();
        }

        order.setOrderItems(items);
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    // Lấy đơn hàng theo user
    public List<Order> getOrdersByAccount(Long accountId) {
        return orderRepository.findByAccountId(accountId);
    }

    // Lấy chi tiết đơn hàng
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    // Cập nhật trạng thái đơn hàng
    public Order updateOrderStatus(Long orderId, String newStatus) {
        // Validate status
        if (!VALID_STATUSES.contains(newStatus)) {
            throw new RuntimeException("Invalid status. Valid statuses: " + String.join(", ", VALID_STATUSES));
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        String currentStatus = order.getStatus();

        // Kiểm tra xem có thể chuyển đổi trạng thái không
        if (!canTransitionTo(currentStatus, newStatus)) {
            throw new RuntimeException(
                    String.format("Cannot transition from %s to %s. Valid transitions: %s",
                            currentStatus,
                            newStatus,
                            String.join(", ", VALID_TRANSITIONS.get(currentStatus))
                    )
            );
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // Kiểm tra chuyển đổi trạng thái hợp lệ
    private boolean canTransitionTo(String currentStatus, String newStatus) {
        // Nếu trạng thái hiện tại giống trạng thái mới, không cần chuyển
        if (currentStatus.equals(newStatus)) {
            return false;
        }

        List<String> allowedTransitions = VALID_TRANSITIONS.get(currentStatus);
        return allowedTransitions != null && allowedTransitions.contains(newStatus);
    }

    // Hủy đơn hàng
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));

        // Chỉ có thể hủy đơn hàng khi chưa giao
        if (order.getStatus().equals("DELIVERED")) {
            throw new RuntimeException("Cannot cancel order that has been delivered");
        }

        if (order.getStatus().equals("CANCELLED")) {
            throw new RuntimeException("Order is already cancelled");
        }

        order.setStatus("CANCELLED");
        return orderRepository.save(order);
    }

    // Lấy tất cả đơn hàng
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Lấy đơn hàng theo status
    public List<Order> getOrdersByStatus(String status) {
        if (!VALID_STATUSES.contains(status)) {
            throw new RuntimeException("Invalid status. Valid statuses: " + String.join(", ", VALID_STATUSES));
        }
        return orderRepository.findByStatus(status);
    }
}