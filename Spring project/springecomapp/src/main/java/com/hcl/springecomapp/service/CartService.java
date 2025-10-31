package com.hcl.springecomapp.service;


import java.util.List;

import com.hcl.springecomapp.payload.CartDTO;

public interface CartService {

    // Add product to cart for the current user
    CartDTO addProductsToCart(Long productId, Integer quantity);

    // Get all carts (admin or for testing)
    List<CartDTO> getAllCarts();

    // Get logged-in user's cart
    CartDTO getCart();

    // Update quantity (increase or decrease) for a product in user's cart
    CartDTO updateProductQuantityInCart(Long productId, Integer operation);

    // Delete a product from a specific cart
    String deleteProductFromCart(Long cartId, Long productId);
}
