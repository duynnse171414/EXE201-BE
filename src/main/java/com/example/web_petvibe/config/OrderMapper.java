package com.example.web_petvibe.config;

import com.example.web_petvibe.entity.Order;
import com.example.web_petvibe.entity.OrderDetails;
import com.example.web_petvibe.model.response.OrderResponse;
import com.example.web_petvibe.service.QRPaymentService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    /**
     * Map Order entity sang OrderResponse (không có payment info)
     */
    public OrderResponse toResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setAccountName(order.getAccount().getFullName());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setShippingAddress(order.getShippingAddress());
        response.setPhoneContact(order.getPhoneContact());
        response.setNote(order.getNote());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setItems(order.getOrderItems().stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toList()));

        return response;
    }

    /**
     * Map Order entity sang OrderResponse có kèm payment info
     */
    public OrderResponse toResponseWithPayment(Order order, QRPaymentService.PaymentInfo paymentInfo) {
        OrderResponse response = toResponse(order);

        if (paymentInfo != null) {
            OrderResponse.PaymentInfoResponse paymentInfoResponse = new OrderResponse.PaymentInfoResponse();
            paymentInfoResponse.setQrCodeUrl(paymentInfo.getQrCodeUrl());
            paymentInfoResponse.setBankId(paymentInfo.getBankId());
            paymentInfoResponse.setAccountNo(paymentInfo.getAccountNo());
            paymentInfoResponse.setAccountName(paymentInfo.getAccountName());
            paymentInfoResponse.setAmount(paymentInfo.getAmount());
            paymentInfoResponse.setDescription(paymentInfo.getDescription());
            paymentInfoResponse.setMessage("Vui lòng quét mã QR để thanh toán đơn hàng. Nội dung chuyển khoản: " + paymentInfo.getDescription());

            response.setPaymentInfo(paymentInfoResponse);
        }

        return response;
    }

    /**
     * Map OrderDetails sang OrderItemResponse
     */
    private OrderResponse.OrderItemResponse toOrderItemResponse(OrderDetails orderDetails) {
        OrderResponse.OrderItemResponse itemResponse = new OrderResponse.OrderItemResponse();
        itemResponse.setProductId(orderDetails.getProduct().getId());
        itemResponse.setProductName(orderDetails.getProduct().getName());
        itemResponse.setQuantity(orderDetails.getQuantity());
        itemResponse.setPrice(orderDetails.getPrice());

        return itemResponse;
    }
}