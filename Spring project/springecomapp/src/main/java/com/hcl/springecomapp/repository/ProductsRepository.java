package com.hcl.springecomapp.repository;

import com.hcl.springecomapp.entity.Products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long>{
	public boolean existsByProductNameIgnoreCase(String productName);
	 List<Products> findByProductNameContainingIgnoreCase(String keyword);
}
