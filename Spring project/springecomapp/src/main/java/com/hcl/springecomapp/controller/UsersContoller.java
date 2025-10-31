
package com.hcl.springecomapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcl.springecomapp.entity.Users;
import com.hcl.springecomapp.service.UsersService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UsersContoller {

	@Autowired
	UsersService usersService;

	@PostMapping("/admin/user")
	public ResponseEntity<Users> addUsers(@RequestBody Users users) {
		System.out.println("To Add" + users);
		Users savedUsers = usersService.saveUser(users);
		return new ResponseEntity<Users>(savedUsers, HttpStatus.CREATED);

	}

	@GetMapping("/public/user")
	public ResponseEntity<List<Users>> getAllUserss() {
		List<Users> usersList = usersService.getAllUsers();
		return new ResponseEntity<List<Users>>(usersList, HttpStatus.OK);
	}

	@PutMapping("/admin/users/{userId}")
	public ResponseEntity<Users> updateUsers(@PathVariable Long userId, @RequestBody Users users) {
		Users savedUsers = usersService.updateUser(userId, users);
		return new ResponseEntity<Users>(savedUsers, HttpStatus.OK);
	}

	@DeleteMapping("/admin/users/{userId}")
	public ResponseEntity<String> deleteUsers(@PathVariable Long userId) {

	 usersService.deleteUser(userId);
		return new ResponseEntity<>("User with ID : " + userId +" Deleted Successfully!!! ", HttpStatus.OK);
	}
	
	@GetMapping("/public/user/{userId}")
	public ResponseEntity<Users> getUserById(@PathVariable Long userId) {
		Users user = usersService.findUsersById(userId);
		return new ResponseEntity<Users>(user, HttpStatus.OK);
	}
	
	

}
