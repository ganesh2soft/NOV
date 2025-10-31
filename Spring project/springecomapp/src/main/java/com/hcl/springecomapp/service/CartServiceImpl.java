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
        // 1. Get logged-in user
        Users user = usersService.getLoggedInUser();

        // 2. Get or create user's cart
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setTotalPrice(0.0);
        }

        // 3. Get product
        Products product = productsRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        // 4. Check stock and regenerate if needed
        if (product.getQuantity() < quantity) {
            if (product.getQuantity() == 0) {
                try {
                    int regeneratedStock = stockClient.generateStock();
                    product.setQuantity(regeneratedStock);
                } catch (Exception e) {
                    throw new OutOfStockException(product.getProductName(), quantity, product.getQuantity());
                }
            }

            if (product.getQuantity() < quantity) {
                throw new OutOfStockException(product.getProductName(), quantity, product.getQuantity());
            }
        }

        // 5. Add or update cart item
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setDiscount(product.getDiscount());
            cart.getCartItems().add(cartItem); // ensure bidirectional mapping
        }

        // 6. Update product stock and cart total
        product.setQuantity(product.getQuantity() - quantity);
        double totalPrice = cart.getTotalPrice() + (product.getSpecialPrice() * quantity);
        cart.setTotalPrice(totalPrice);

        // 7. Save entities
        productsRepository.save(product);
        cartRepository.save(cart);
        cartItemRepository.save(cartItem);

        // 8. Convert to DTO
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> productsDTO = cart.getCartItems()
                .stream()
                .map(ci -> {
                    ProductDTO dto = modelMapper.map(ci.getProduct(), ProductDTO.class);
                    dto.setQuantity(ci.getQuantity()); // include cart quantity
                    return dto;
                })
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
