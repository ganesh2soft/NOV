package com.hcl.springecomapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hcl.springecomapp.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.springecomapp.exception.APIException;
import com.hcl.springecomapp.exception.ResourceNotFoundException;
import com.hcl.springecomapp.payload.OrderDTO;
import com.hcl.springecomapp.payload.OrderItemDTO;
import com.hcl.springecomapp.repository.CartItemRepository;
import com.hcl.springecomapp.repository.CartRepository;
import com.hcl.springecomapp.repository.OrderItemRepository;
import com.hcl.springecomapp.repository.OrderRepository;
import com.hcl.springecomapp.repository.PaymentRepository;
import com.hcl.springecomapp.repository.ProductsRepository;
import com.hcl.springecomapp.util.AuthUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	ProductsRepository productRepository;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CartService cartService;

	@Autowired
	private AuthUtil authUtil;

	/*
	 * @Override public OrderDTO placeOrder(String emailId, String address, String
	 * paymentMethod, String pgName, String pgPaymentId, String pgStatus, String
	 * pgResponseMessage) { //Get the users cart Cart cart =
	 * cartRepository.findCartByEmail(emailId); if (cart == null) { throw new
	 * ResourceNotFoundException("Cart", "email", emailId); } Order order = new
	 * Order(); order.setEmail(emailId); order.setOrderDate(LocalDate.now());
	 * order.setTotalAmount(cart.getTotalPrice()); order.setOrderStatus("Accepted");
	 * order.setAddress(address);
	 * 
	 * Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus,
	 * pgResponseMessage, pgName); payment.setOrder(order); payment =
	 * paymentRepository.save(payment); order.setPayment(payment); Order savedOrder
	 * = orderRepository.save(order);
	 * 
	 * List<CartItem> cartItems = new ArrayList<>(cart.getCartItems()); if
	 * (cartItems.isEmpty()) { throw new APIException("Cart is empty"); }
	 * 
	 * List<OrderItem> orderItems = new ArrayList<>(); for (CartItem cartItem :
	 * cartItems) { OrderItem orderItem = new OrderItem();
	 * orderItem.setProduct(cartItem.getProduct());
	 * orderItem.setQuantity(cartItem.getQuantity());
	 * orderItem.setDiscount(cartItem.getDiscount());
	 * orderItem.setOrderedProductPrice(cartItem.getProductPrice());
	 * orderItem.setOrder(savedOrder); orderItems.add(orderItem); } orderItems =
	 * orderItemRepository.saveAll(orderItems);
	 * 
	 * for (CartItem item : cartItems) { Products product = item.getProduct(); int
	 * quantity = item.getQuantity();
	 * 
	 * product.setQuantity(product.getQuantity() - quantity);
	 * productRepository.save(product);
	 * 
	 * //cartService.deleteProductFromCart(cart.getCartId(),
	 * item.getProduct().getId()); cartItemRepository.deleteAll(cartItems);
	 * cart.getCartItems().clear(); cartRepository.save(cart); }
	 * 
	 * OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
	 * orderItems.forEach(item -> orderDTO.getOrderItems()
	 * .add(modelMapper.map(item, OrderItemDTO.class)));
	 * 
	 * orderDTO.setAddress(address); return orderDTO;
	 * 
	 * }
	 */

	@Override
	public OrderDTO placeOrder(String emailId, String address, String paymentMethod, String pgName, String pgPaymentId,
			String pgStatus, String pgResponseMessage) {

		String userEmailId = authUtil.loggedInEmail();
		System.out.println("orderRequestDTO DATA: " + userEmailId);
		Cart cart = cartRepository.findCartByEmail(userEmailId);
		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "email", emailId);
		}

		Order order = new Order();
		order.setEmail(emailId);
		order.setOrderDate(LocalDate.now());
		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus("Accepted");
		order.setAddress(address);

		Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
		payment.setOrder(order);
		payment = paymentRepository.save(payment);
		order.setPayment(payment);

		Order savedOrder = orderRepository.save(order);

		List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
		if (cartItems.isEmpty()) {
			throw new APIException("Cart is empty");
		}

		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			orderItem.setOrder(savedOrder);

			orderItems.add(orderItem);

			// ✅ Reduce stock
			Products product = cartItem.getProduct();
			product.setQuantity(product.getQuantity() - cartItem.getQuantity());
			productRepository.save(product);
		}

		orderItems = orderItemRepository.saveAll(orderItems);

		// Clear cart only once AFTER loop
		cartItemRepository.deleteAll(cartItems);
		cart.getCartItems().clear();
		cartRepository.save(cart);

		// Prepare Response DTO
		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
		orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

		orderDTO.setAddress(address);
		return orderDTO;
	}

}
