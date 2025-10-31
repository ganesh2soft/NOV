package com.hcl.springecomapp.service;

import com.hcl.springecomapp.payload.OrderDTO;

import jakarta.transaction.Transactional;

public interface OrderService {

	  @Transactional
	  OrderDTO placeOrder(String emailId, String address, 
			  String paymentMethod, String pgName, String pgPaymentId,
			  String pgStatus, String pgResponseMessage);

}
