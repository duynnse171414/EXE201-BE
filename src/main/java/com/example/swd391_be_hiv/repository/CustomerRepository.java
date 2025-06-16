package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Tìm tất cả customer chưa bị xóa
    @Query("SELECT c FROM Customer c WHERE c.isDeleted = false")
    List<Customer> findAllActive();

    // Tìm customer theo ID và chưa bị xóa
    @Query("SELECT c FROM Customer c WHERE c.Id = ?1 AND c.isDeleted = false")
    Optional<Customer> findByIdActive(Long id);

    // Tìm customer theo account ID
    @Query("SELECT c FROM Customer c WHERE c.account.id = ?1 AND c.isDeleted = false")
    List<Customer> findByAccountId(Long accountId);

    // Kiểm tra xem account đã có customer chưa
    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.account.id = ?1 AND c.isDeleted = false")
    boolean existsByAccountId(Long accountId);
}
