package com.hcl.springecomapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.springecomapp.entity.Order;


public interface OrderRepository  extends JpaRepository<Order, Long>{

}
