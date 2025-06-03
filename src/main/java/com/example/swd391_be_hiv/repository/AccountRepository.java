package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // lấy account bằng phone
    Account findAccountByPhone(String phone);

    //Account findByUsername(String username);

Account findByEmail(String email);

    Account findAccountById(Long id);


}
