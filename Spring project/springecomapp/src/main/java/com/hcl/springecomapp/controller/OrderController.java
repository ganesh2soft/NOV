package com.hcl.springecomapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.springecomapp.payload.OrderDTO;
import com.hcl.springecomapp.payload.OrderRequestDTO;
import com.hcl.springecomapp.service.OrderService;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
	
	@Autowired
    private OrderService orderService;
	@PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable String paymentMethod, 
    		@RequestBody OrderRequestDTO orderRequestDTO,Authentication authentication) {
     
    	 //String emailId = "admin@example.com";
		String emailId = authentication.getName();
		System.out.println("Emmmmmmmaaaaaiiiiiillllll " +emailId);
        System.out.println("orderRequestDTO DATA: " + orderRequestDTO);
        OrderDTO order = orderService.placeOrder(emailId,
                orderRequestDTO.getAddress(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
	
	

}
