package com.hcl.springecomapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.springecomapp.entity.OrderItem;

public interface OrderItemRepository  extends JpaRepository<OrderItem, Long>{

}
