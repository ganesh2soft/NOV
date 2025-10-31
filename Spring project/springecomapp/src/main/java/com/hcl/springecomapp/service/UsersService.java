package com.hcl.springecomapp.service;

import java.util.List;

import com.hcl.springecomapp.entity.Users;

public interface UsersService {
	public Users saveUser(Users user);
	public List<Users> getAllUsers();
	
	public Users updateUser(Long userId, Users users);
	public Users findUsersById(Long userId);
	public void deleteUser(Long userId);
	public Users getLoggedInUser();
	
}
