package com.hcl.springecomapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hcl.springecomapp.entity.Cart;
import com.hcl.springecomapp.entity.Users;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	@Query("select c from Cart c where c.user.email =?1") 	
	Cart findCartByEmail(String emailId);
	
	
	Cart findByUser(Users user);
}
