package com.example.web_petvibe.config;

import com.example.web_petvibe.entity.Order;
import com.example.web_petvibe.entity.OrderDetails;
import com.example.web_petvibe.model.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setShippingAddress(order.getShippingAddress());
        response.setPhoneContact(order.getPhoneContact());
        response.setTotalAmount(order.getTotalAmount());
        response.setCreatedAt(order.getCreatedAt());
        response.setAccountName(order.getAccount().getFullName());

        response.setItems(order.getOrderItems().stream().map(item -> {
            OrderResponse.OrderItemResponse itemRes = new OrderResponse.OrderItemResponse();
            itemRes.setProductId(item.getProduct().getId());
            itemRes.setProductName(item.getProduct().getName());
            itemRes.setQuantity(item.getQuantity());
            itemRes.setPrice(item.getPrice());
            return itemRes;
        }).collect(Collectors.toList()));

        return response;
    }
}

