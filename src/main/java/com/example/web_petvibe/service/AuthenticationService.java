package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.Account;
import com.example.web_petvibe.exception.DuplicateEntity;
import com.example.web_petvibe.exception.NotFoundException;
import com.example.web_petvibe.model.response.AccountResponse;
import com.example.web_petvibe.model.request.LoginRequest;
import com.example.web_petvibe.model.request.RegisterRequest;
import com.example.web_petvibe.model.request.ResetPasswordRequest;
import com.example.web_petvibe.model.request.UpdateAccountRequest;
import com.example.web_petvibe.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public AccountResponse register(RegisterRequest registerRequest) {
        Account account = modelMapper.map(registerRequest, Account.class);

        if (accountRepository.findAccountByPhone(account.getPhone()) != null) {
            throw new DuplicateEntity("Duplicate phone!");
        }

        try {
            String originPassword = account.getPassword();
            account.setPassword(passwordEncoder.encode(originPassword));

            Account newAccount = accountRepository.save(account);
            return modelMapper.map(newAccount, AccountResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred: " + e.getMessage());
        }
    }

    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));
    }

    public AccountResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getPhone(),
                            loginRequest.getPassword()
                    ));
            Account account = (Account) authentication.getPrincipal();
            AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
            accountResponse.setToken(tokenService.generateToken(account));
            return accountResponse;
        } catch (Exception e) {
            throw new NotFoundException("Username or password invalid!");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        return accountRepository.findAccountByPhone(phone);
    }

    // Lấy account đang đăng nhập
    public Account getCurrentAccount() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findAccountById(account.getId());
    }

    // Đổi mật khẩu khi đang đăng nhập
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        Account account = getCurrentAccount();

        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New password and confirm password do not match.");
        }
        if (newPassword.equals(currentPassword)) {
            throw new IllegalArgumentException("New password cannot be the same as the current password.");
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    // Xóa mềm account
    public Account deleteAccount(long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }
        account.setDeleted(true);
        return accountRepository.save(account);
    }

    // Khôi phục account
    public Account restoreAccount(long accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new NotFoundException("Account not found");
        }
        if (!account.isDeleted()) {
            throw new IllegalStateException("Account is not deleted");
        }
        account.setDeleted(false);
        return accountRepository.save(account);
    }

    // Update thông tin account
    public Account updateAccount(Long accountId, UpdateAccountRequest request) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found with id: " + accountId));

        if (request.getFullName() != null) {
            existingAccount.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            existingAccount.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            Account accountWithSamePhone = accountRepository.findAccountByPhone(request.getPhone());
            if (accountWithSamePhone != null && accountWithSamePhone.getId() != existingAccount.getId()) {
                throw new DuplicateEntity("Phone number already in use!");
            }
            existingAccount.setPhone(request.getPhone());
        }
        if (request.getPetName() != null) {
            existingAccount.setPetName(request.getPetName());
        }
        if (request.getPetAge() != null) {
            existingAccount.setPetAge(request.getPetAge());
        }
        if (request.getPetType() != null) {
            existingAccount.setPetType(request.getPetType());
        }
        if (request.getPetSize() != null) {
            existingAccount.setPetSize(request.getPetSize());
        }

        return accountRepository.save(existingAccount);
    }



    private boolean isOtpExpired(Long expirationTime) {
        return expirationTime == null || System.currentTimeMillis() > expirationTime;
    }

    // Reset mật khẩu không cần OTP
    public void resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();
        String newPassword = request.getNewPassword();
        String confirmPassword = request.getConfirmPassword();

        // Tìm user theo email
        Account user = accountRepository.findAccountByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("New password and confirm password do not match.");
        }

        // Encode password mới
        user.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(user);
    }


}
