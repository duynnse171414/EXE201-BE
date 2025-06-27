package com.example.swd391_be_hiv.api;
import com.example.swd391_be_hiv.entity.Customer;
import com.example.swd391_be_hiv.model.reponse.ApiResponse;
import com.example.swd391_be_hiv.model.reponse.CreateCustomerResponse;
import com.example.swd391_be_hiv.model.reponse.GetCustomerResponse;
import com.example.swd391_be_hiv.model.request.CustomerRequest;
import com.example.swd391_be_hiv.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@SecurityRequirement(name = "api")

public class CustomerAPI {

    @Autowired
    private CustomerService customerService;


    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        try {
            Optional<Customer> customer = customerService.getCustomerById(id);
            if (customer.isPresent()) {
                return ResponseEntity.ok(new GetCustomerResponse("Customer found successfully", true, customer.get().getAccount()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Customer not found with id: " + id, false));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error occurred while getting customer", false));
        }
    }



    // POST /api/customers - Tạo mới customer với account_id
//    @PostMapping
//    public ResponseEntity<?> createCustomer(@Valid @RequestBody CustomerRequest request) {
//        try {
//            Customer createdCustomer = customerService.createCustomer(request.getAccountId());
//            return ResponseEntity.status(HttpStatus.CREATED).body(new CreateCustomerResponse("Customer created successfully", true, createdCustomer));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error occurred while creating customer", false));
//        }
//    }
    // PUT /api/customers/{id} - Cập nhật customer
//    @PutMapping("/{id}")
//    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
//        try {
//            Customer updatedCustomer = customerService.updateCustomer(id, customer);
//            return ResponseEntity.ok(updatedCustomer);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    // DELETE /api/customers/{id} - Xóa mềm customer
    // DELETE /api/customers/{id} - Xóa mềm customer
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok("Customer deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found with id: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while deleting customer");
        }
    }
    // GET /api/customers/account/{accountId} - Lấy customer theo account ID
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Customer>> getCustomersByAccountId(@PathVariable Long accountId) {
        try {
            List<Customer> customers = customerService.getCustomersByAccountId(accountId);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

