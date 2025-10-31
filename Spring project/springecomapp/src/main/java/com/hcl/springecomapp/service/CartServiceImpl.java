package com.hcl.springecomapp.service;

import java.util.List;
import java.util.stream.Collectors;

import com.hcl.springecomapp.client.StockClient;
import com.hcl.springecomapp.entity.Products;
import com.hcl.springecomapp.exception.InventoryServiceUnavailableException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.springecomapp.entity.Cart;
import com.hcl.springecomapp.entity.CartItem;
import com.hcl.springecomapp.entity.Users;
import com.hcl.springecomapp.exception.OutOfStockException;
import com.hcl.springecomapp.exception.ResourceNotFoundException;
import com.hcl.springecomapp.payload.CartDTO;
import com.hcl.springecomapp.payload.ProductDTO;
import com.hcl.springecomapp.repository.CartItemRepository;
import com.hcl.springecomapp.repository.CartRepository;
import com.hcl.springecomapp.repository.ProductsRepository;
import com.hcl.springecomapp.repository.UsersRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductsRepository productsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StockClient stockClient;

    /**
     * Add product to cart for the logged-in user
     */
    @Override
    public CartDTO addProductsToCart(Long productId, Integer quantity) {
        // Get logged-in user
        Users user = usersService.getLoggedInUser();

        // Get user's cart or create new
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setTotalPrice(0.0);
            cartRepository.save(cart);
        }

        // Get product
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // Check stock


        if (product.getQuantity() < quantity) {
            // If stock is 0, try to regenerate from InventoryService
            if (product.getQuantity() == 0) {
                int regeneratedStock;
                try {
                    regeneratedStock = stockClient.generateStock();
                } catch (Exception e) {
                    throw new OutOfStockException(product.getProductName(), quantity, product.getQuantity());
                }

                product.setQuantity(regeneratedStock);
                productsRepository.save(product); // Save updated stock
            }

            // Re-check after regeneration
            if (product.getQuantity() < quantity) {
                throw new OutOfStockException(product.getProductName(), quantity, product.getQuantity());
            }
        }

        //product.setQuantity(generatedStock);


        // Check if product already exists in cart
        CartItem existingItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setProductPrice(product.getSpecialPrice());
            newItem.setDiscount(product.getDiscount());
            cartItemRepository.save(newItem);
        }

        // Update total and stock
        product.setQuantity(product.getQuantity() - quantity);
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
        productsRepository.save(product);
        cartRepository.save(cart);

        // Convert to DTO
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> productsDTO = cart.getCartItems()
                .stream()
                .map(ci -> modelMapper.map(ci.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());
        cartDTO.setProducts(productsDTO);

        return cartDTO;
    }

    /**
     * Get all carts (admin)
     */
    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        return carts.stream().map(cart -> modelMapper.map(cart, CartDTO.class)).collect(Collectors.toList());
    }

    /**
     * Get logged-in user's cart
     */
    @Override
    public CartDTO getCart() {
        Users user = usersService.getLoggedInUser();
        Cart cart = cartRepository.findByUser(user);
        if (cart == null)
            throw new ResourceNotFoundException("Cart", "user", user.getEmail());
        return modelMapper.map(cart, CartDTO.class);
    }

    /**
     * Update product quantity (+1 or -1) in logged-in user's cart
     */
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer operation) {
        Users user = usersService.getLoggedInUser();
        Cart cart = cartRepository.findByUser(user);
        if (cart == null)
            throw new ResourceNotFoundException("Cart", "user", user.getEmail());

        CartItem item = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if (item == null)
            throw new ResourceNotFoundException("CartItem", "productId", productId);

        Products product = item.getProduct();

        int newQuantity = item.getQuantity() + operation;
        if (newQuantity <= 0) {
            // Remove item if quantity <= 0
            cartItemRepository.delete(item);
            cart.setTotalPrice(cart.getTotalPrice() - (item.getQuantity() * item.getProductPrice()));
        } else {
            // Update stock and totals

            if (operation > 0) {
                if (product.getQuantity() < operation) {
                    if (product.getQuantity() == 0) {
                        int regeneratedStock;
                        try {
                            regeneratedStock = stockClient.generateStock();
                        } catch (Exception e) {
                            throw new InventoryServiceUnavailableException("Inventory Service is currently.");
                        }

                        product.setQuantity(regeneratedStock);
                        productsRepository.save(product);
                    }

                    // Re-check after regeneration
                    if (product.getQuantity() < operation) {
                        throw new OutOfStockException(product.getProductName(), operation, product.getQuantity());
                    }
                }

                product.setQuantity(product.getQuantity() - operation);
            }

            item.setQuantity(newQuantity);
            cart.setTotalPrice(cart.getTotalPrice() + (item.getProductPrice() * operation));
            cartItemRepository.save(item);
        }

        productsRepository.save(product);
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> productsDTO = cart.getCartItems()
                .stream()
                .map(ci -> modelMapper.map(ci.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());
        cartDTO.setProducts(productsDTO);

        return cartDTO;
    }

    /**
     * Delete product from cart
     */
    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        // Step 1: Find cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        // Step 2: Find cart item by cartId and productId
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if (cartItem == null)
            throw new ResourceNotFoundException("CartItem", "productId", productId);

        // Step 3: Update cart total
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getQuantity() * cartItem.getProductPrice()));

        // Step 4: Delete cart item
        cartItemRepository.delete(cartItem);

        // Step 5: Save updated cart
        cartRepository.save(cart);

        return "Product removed from cart successfully";
    }

}
