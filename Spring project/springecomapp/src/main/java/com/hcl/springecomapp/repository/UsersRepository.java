package com.hcl.springecomapp.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.springecomapp.entity.AppRole;
import com.hcl.springecomapp.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{
	
	public Optional<Users> findByUserName(String username);
	public Optional<Users> findByEmail(String email);
	public Users findUsersByEmail(String email);
	public boolean existsByUserName(String username);

	public boolean existsByEmail(String email);

	public Optional<AppRole> findByRoles(AppRole role);

	
}
