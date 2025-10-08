package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.Account;
import com.example.web_petvibe.exception.DuplicateEntity;
import com.example.web_petvibe.exception.NotFoundException;
import com.example.web_petvibe.model.response.AccountResponse;
import com.example.web_petvibe.model.request.LoginRequest;
import com.example.web_petvibe.model.request.RegisterRequest;
import com.example.web_petvibe.model.request.ResetPasswordRequest;
import com.example.web_petvibe.model.request.UpdateAccountRequest;
import com.example.web_petvibe.repository.AccountRepository;
import com.example.web_petvibe.service.AuthenticationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SecurityRequirement(name = "api")
@RestController
@RequestMapping("api")
public class AuthenticationAPI {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {
        AccountResponse newAccount = authenticationService.register(registerRequest);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        AccountResponse newAccount = authenticationService.login(loginRequest);
        return ResponseEntity.ok(newAccount);
    }

    @GetMapping("accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = authenticationService.getAllAccount();
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("accounts/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody UpdateAccountRequest request) {
        try {
            Account account = authenticationService.updateAccount(id, request);
            return ResponseEntity.ok(account);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (DuplicateEntity e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred while updating account");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        try {
            Account account = authenticationService.deleteAccount(id);
            return ResponseEntity.ok("Account with id " + id + " has been deleted successfully.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting account");
        }
    }

    //  Restore account
    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreAccount(@PathVariable Long id) {
        try {
            Account account = authenticationService.restoreAccount(id);
            return ResponseEntity.ok("Account with id " + id + " has been restored successfully.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while restoring account");
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            authenticationService.resetPassword(request);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}