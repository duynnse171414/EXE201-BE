package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.Account;
import com.example.swd391_be_hiv.entity.Customer;
import com.example.swd391_be_hiv.repository.AccountRepository;
import com.example.swd391_be_hiv.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
@Autowired
     CustomerRepository customerRepository;
@Autowired
AccountRepository accountRepository;

    // Lấy tất cả customer
    public List<Customer> getAllCustomers() {
        return customerRepository.findAllActive();
    }

    // Lấy customer theo ID
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findByIdActive(id);
    }

    // Tạo mới customer với account_id
    public Customer createCustomer(Long accountId) {
        // Tìm account theo ID
        Optional<Account> account = accountRepository.findById(accountId);
        if (!account.isPresent()) {
            throw new RuntimeException("Account not found with id: " + accountId);
        }

        // Kiểm tra xem account đã có customer chưa
        if (customerRepository.existsByAccountId(accountId)) {
            throw new RuntimeException("Account with id: " + accountId + " already has a customer");
        }

        // Tạo customer mới
        Customer customer = new Customer();
        customer.setDeleted(false);
        customer.setAccount(account.get());

        return customerRepository.save(customer);
    }

//    // Cập nhật customer
//    public Customer updateCustomer(Long id, Customer customerDetails) {
//        Optional<Customer> existingCustomer = customerRepository.findByIdActive(id);
//        if (existingCustomer.isPresent()) {
//            Customer customer = existingCustomer.get();
//            // Cập nhật các field cần thiết
//            customer.setAccount(customerDetails.getAccount());
//            customer.setDeleted(customerDetails.getAccount().isDeleted());
//            return customerRepository.save(customer);
//        }
//        throw new RuntimeException("Customer not found with id: " + id);
//    }

    // Xóa mềm customer
    public void deleteCustomer(Long id) {
        Optional<Customer> customer = customerRepository.findByIdActive(id);
        if (customer.isPresent()) {
            Customer c = customer.get();
            c.setDeleted(true);
            customerRepository.save(c);
        } else {
            throw new RuntimeException("Customer not found with id: " + id);
        }
    }

    // Lấy customer theo account ID
    public List<Customer> getCustomersByAccountId(Long accountId) {
        return customerRepository.findByAccountId(accountId);
    }
}