package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    @Query("SELECT c FROM Customer c WHERE c.isDeleted = false")
    List<Customer> findAllActive();


    @Query("SELECT c FROM Customer c WHERE c.Id = ?1 AND c.isDeleted = false")
    Optional<Customer> findByIdActive(Long id);


    @Query("SELECT c FROM Customer c WHERE c.account.id = ?1 AND c.isDeleted = false")
    List<Customer> findByAccountId(Long accountId);


    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.account.id = ?1 AND c.isDeleted = false")
    boolean existsByAccountId(Long accountId);

    // Thêm các method này vào CustomerRepository interface:

    @Query("SELECT c FROM Customer c WHERE c.account.id = :accountId AND c.isDeleted = false")
    Optional<Customer> findByAccountIdActive(@Param("accountId") Long accountId);


}
