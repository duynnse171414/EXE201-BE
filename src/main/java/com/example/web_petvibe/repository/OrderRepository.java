package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccountId(Long accountId);
    /**
     * Kiểm tra xem account đã mua sản phẩm và đơn hàng đã hoàn thành thành công
     * Status = 'DELIVERED' (đã giao hàng thành công)
     */
    @Query("SELECT COUNT(o) > 0 FROM Order o " +
            "JOIN o.orderItems oi " +
            "WHERE o.account.id = ?1 " +
            "AND oi.product.id = ?2 " +
            "AND o.status = 'DELIVERED'")
    boolean existsByAccountIdAndProductIdAndOrderCompleted(Long accountId, Long productId);

    /**
     * Kiểm tra với nhiều status (SHIPPED hoặc DELIVERED)
     * Nếu muốn cho phép review khi đơn hàng đang giao hoặc đã giao
     */
    @Query("SELECT COUNT(o) > 0 FROM Order o " +
            "JOIN o.orderItems oi " +
            "WHERE o.account.id = ?1 " +
            "AND oi.product.id = ?2 " +
            "AND o.status IN ('SHIPPED', 'DELIVERED')")
    boolean existsByAccountIdAndProductIdAndOrderShippedOrDelivered(Long accountId, Long productId);

    /**
     * Kiểm tra đơn hàng đã thanh toán (trừ PENDING và CANCELLED)
     */
    @Query("SELECT COUNT(o) > 0 FROM Order o " +
            "JOIN o.orderItems oi " +
            "WHERE o.account.id = ?1 " +
            "AND oi.product.id = ?2 " +
            "AND o.status NOT IN ('PENDING', 'CANCELLED')")
    boolean existsByAccountIdAndProductIdAndOrderPaid(Long accountId, Long productId);

    @Query("SELECT o FROM Order o WHERE o.status = ?1 ORDER BY o.createdAt DESC")
    List<Order> findByStatus(String status);
}
