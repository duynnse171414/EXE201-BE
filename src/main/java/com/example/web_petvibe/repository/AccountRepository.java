package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // lấy account bằng phone
    Account findAccountByPhone(String phone);

    Account findAccountByEmail(String email);

    // Trả về Optional để dùng orElseThrow()


//Account findByEmail(String email);
//
Account findAccountById(Long id);
//

}
