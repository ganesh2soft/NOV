package com.hcl.springecomapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hcl.springecomapp.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
