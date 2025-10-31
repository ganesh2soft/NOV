package com.hcl.springecomapp.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.springecomapp.entity.Cart;
import com.hcl.springecomapp.payload.CartDTO;
import com.hcl.springecomapp.payload.MessageResponse;
import com.hcl.springecomapp.service.CartService;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

	@Autowired
	CartService cartService;
	
	  @PostMapping("/products/{productId}/quantity/{quantity}")
	    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
	        CartDTO cartDTO = cartService.addProductsToCart(productId, quantity);
	        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
	    }

	    @GetMapping
	    public ResponseEntity<List<CartDTO>> getCarts() {
	        List<CartDTO> cartDTOs = cartService.getAllCarts();
	        return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
	    }

	    @GetMapping("/users/cart")
	    public ResponseEntity<CartDTO> getCartById() {
	        CartDTO cartDTO = cartService.getCart();
	        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
	    }

	    @PutMapping("/products/{productId}/quantity/{operation}")
	    public ResponseEntity<CartDTO> updateProductQuantityInCart(@PathVariable Long productId, @PathVariable String operation) {
	        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, operation.equalsIgnoreCase("delete") ? -1 : 1);
	        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
	    }

	    @DeleteMapping("/{cartId}/product/{productId}")
	    public ResponseEntity<MessageResponse> deleteProductFromCart(@PathVariable Long cartId,
	                                                                 @PathVariable Long productId) {
	        String status = cartService.deleteProductFromCart(cartId, productId);
	        MessageResponse messageResponse = new MessageResponse(status);
	        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
	    }
/*
	@PostMapping("/public/cart")
	public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
		Cart savedCart = cartService.createCart(cart);
		return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
	}

	@PostMapping("/public/cart/{cartId}/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long cartId, @PathVariable Long productId,
			@PathVariable Integer quantity) {

		System.out.println(cartId + " ---------------------------------");
		CartDTO cartDTO = cartService.addProductToCart(cartId, productId, quantity);

		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
	}

	@GetMapping("/admin/carts")
	public ResponseEntity<List<CartDTO>> getCarts() {
		List<CartDTO> cartDTOs = cartService.getAllCarts();
		return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
	}

	@GetMapping("/public/users/{emailId}/carts/{cartId}")
	public ResponseEntity<CartDTO> getCartById(@PathVariable String emailId, @PathVariable Long cartId) {
		CartDTO cartDTO = cartService.getCart(emailId, cartId);
		return new ResponseEntity<>(cartDTO, HttpStatus.OK);
	}

	@PutMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long cartId, @PathVariable Long productId,
			@PathVariable Integer quantity) {
		CartDTO cartDTO = cartService.updateProductQuantityInCart(cartId, productId, quantity);
		return new ResponseEntity<>(cartDTO, HttpStatus.OK);
	}

	@DeleteMapping("/public/carts/{cartId}/products/{productId}")
	public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
		String status = cartService.deleteProductFromCart(cartId, productId);
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
*/
}
