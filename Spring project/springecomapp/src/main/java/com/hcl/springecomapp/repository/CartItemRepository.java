package com.hcl.springecomapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hcl.springecomapp.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>{
 
	@Query("select ci from CartItem ci where ci.cart.cartId =?1 and ci.product.id =?2")
	CartItem findCartItemByProductIdAndCartId(Long cartId, Long productId);
}
