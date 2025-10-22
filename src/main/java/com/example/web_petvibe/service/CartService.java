package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.Cart;
import com.example.web_petvibe.entity.Product;
import com.example.web_petvibe.model.response.CartData;
import com.example.web_petvibe.model.response.ProductInCart;
import com.example.web_petvibe.repository.CartRepository;
import com.example.web_petvibe.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    @Autowired
    private final CartRepository cartRepository;

    @Autowired
    private final ProductRepository productRepository;

    // Lấy tất cả cart
    public List<Cart> getAllCarts() {
        return cartRepository.findAllActive();
    }

    // Lấy cart theo ID
    public Optional<Cart> getCartById(Long id) {
        return cartRepository.findByIdActive(id);
    }

    // Lấy cart theo User ID
    public List<Cart> getCartsByUserId(Long userId) {
        return cartRepository.findByUserIdActive(userId);
    }

    // Tạo mới cart với userId và trả về đầy đủ thông tin product
    public CartData createCart(Cart cart, Long userId) {


        // Kiểm tra xem cart item đã tồn tại chưa
        if (cartRepository.existsByUserIdAndProductId(cart.getUserId(), cart.getProductId())) {
            throw new RuntimeException("Cart item already exists for this user and product");
        }

        // Lấy thông tin product
        Optional<Product> productOpt = productRepository.findByIdActive(cart.getProductId());
        if (!productOpt.isPresent()) {
            throw new RuntimeException("Product not found with id: " + cart.getProductId());
        }

        Product product = productOpt.get();

        // Lưu cart
        cart.setDeleted(false);
        Cart savedCart = cartRepository.save(cart);

        // Tạo response với thông tin đầy đủ
        ProductInCart productInCart = new ProductInCart(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl(),
                product.getType(),
                product.getStock()
        );

        CartData cartData = new CartData(
                savedCart.getId(),
                savedCart.getQuantity(),
                savedCart.getTotal(),
                savedCart.getUserId(),
                productInCart
        );

        return cartData;
    }

    // Cập nhật cart
    public Cart updateCart(Long id, Cart cart) {
        Optional<Cart> existingCart = cartRepository.findByIdActive(id);
        if (existingCart.isPresent()) {
            Cart c = existingCart.get();
            c.setQuantity(cart.getQuantity());
            c.setTotal(cart.getTotal());
            c.setProductId(cart.getProductId());
            return cartRepository.save(c);
        } else {
            throw new RuntimeException("Cart not found with id: " + id);
        }
    }

    // Xóa mềm cart
    public void deleteCart(Long id) {
        Optional<Cart> cart = cartRepository.findByIdActive(id);
        if (cart.isPresent()) {
            Cart c = cart.get();
            c.setDeleted(true);
            cartRepository.save(c);
        } else {
            throw new RuntimeException("Cart not found with id: " + id);
        }
    }
}