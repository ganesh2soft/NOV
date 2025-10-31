package com.hcl.springecomapp.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcl.springecomapp.entity.Users;
import com.hcl.springecomapp.exception.UserNotFoundException;
import com.hcl.springecomapp.repository.UsersRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsersServiceImpl implements UsersService{

	@Autowired
	UsersRepository usersRepository;
	
	@Override
	public Users saveUser(Users user) {
		
		return usersRepository.save(user);
	}

	@Override
	public List<Users> getAllUsers() {
		
		return usersRepository.findAll();
	}

	@Override
	public Users updateUser(Long userId, Users users) {
		Optional<Users> userToBeUpdated = usersRepository.findById(userId);
		Users user = null;
		if (userToBeUpdated.get() != null) {
			user = userToBeUpdated.get();
			user.setUserName(users.getUserName());
			user.setEmail(users.getEmail());
			user.setPassword(users.getPassword());
			user.setRoles(users.getRoles());
			user.setAddress(users.getAddress());
		}

		Users savedUsers = usersRepository.save(user);
		return savedUsers;
	}

	@Override
	public Users findUsersById(Long userId) {
		Users user = usersRepository.findById(userId)
				.orElseThrow(()-> new UserNotFoundException("User",userId));
		return user;
	}

	@Override
	public void deleteUser(Long userId) {
		
		Users user = usersRepository.findById(userId)
				.orElseThrow(()-> new UserNotFoundException("User",userId));
		usersRepository.delete(user);
		System.out.println("User Deleted Successfully !!!");
	}

	
	@Override
    public Users getLoggedInUser() {
        // Just assume user with ID = 1 is logged in
        Long userId = 1L;

        // Fetch that user from DB
        return usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

	
}
