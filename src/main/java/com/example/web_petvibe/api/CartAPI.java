package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.Cart;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.CartResponse;
import com.example.web_petvibe.model.response.CartData;
import com.example.web_petvibe.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carts")
@SecurityRequirement(name = "api")
@Tag(name = "Cart", description = "API quản lý giỏ hàng")
public class CartAPI {

    @Autowired
    private CartService cartService;

    // GET all carts - Admin and Staff only
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public ResponseEntity<List<Cart>> getAllCarts() {
        try {
            return ResponseEntity.ok(cartService.getAllCarts());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET cart by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'CUSTOMER')")
    public ResponseEntity<?> getCartById(@PathVariable Long id) {
        Optional<Cart> cart = cartService.getCartById(id);
        return cart.isPresent()
                ? ResponseEntity.ok(new ApiResponse("Cart found successfully", true))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Cart not found with id: " + id, false));
    }

    // GET carts by user id
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'CUSTOMER')")
    public ResponseEntity<List<Cart>> getCartsByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(cartService.getCartsByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST create cart
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'CUSTOMER')")
    public ResponseEntity<?> createCart(@RequestBody Cart cart) {
        try {
            // Gọi service, truyền userId từ cart
            CartData created = cartService.createCart(cart, cart.getUserId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new CartResponse("Cart created successfully", true, created));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        }
    }


    // PUT update cart
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'CUSTOMER')")
    public ResponseEntity<?> updateCart(@PathVariable Long id, @RequestBody Cart cart) {
        try {
            Cart updated = cartService.updateCart(id, cart);
            return ResponseEntity.ok(new ApiResponse("Cart updated successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }

    // DELETE cart
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'CUSTOMER')")
    public ResponseEntity<?> deleteCart(@PathVariable Long id) {
        try {
            cartService.deleteCart(id);
            return ResponseEntity.ok(new ApiResponse("Cart deleted successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }
}