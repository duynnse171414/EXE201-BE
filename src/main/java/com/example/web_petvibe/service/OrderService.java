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

    // Tạo đơn hàng
    public Order createOrder(OrderRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        Order order = new Order();
        order.setAccount(account);
        order.setShippingAddress(request.getShippingAddress());
        order.setPhoneContact(request.getPhoneContact());

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



}
