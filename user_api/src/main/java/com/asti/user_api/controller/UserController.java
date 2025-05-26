package com.asti.user_api.controller;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.asti.user_api.entity.User;
import com.asti.user_api.exception.UserAlreadyExistsException;
import com.asti.user_api.exception.UserNotFoundException;
import com.asti.user_api.service.UserService;

import jakarta.validation.Valid;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/api/v1/user")
	public ResponseEntity<User> createUser(@Valid @RequestBody User userInput){
		
		User user = null;
		
		try {
			user = userService.createUser(userInput);
		} catch (UserAlreadyExistsException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
	
	@GetMapping("/api/v1/user/name/{userName}")
	public ResponseEntity<User> findUserByUserName(@PathVariable("userName") String userName) throws UserNotFoundException{
		
		User user = null;
		
		user = userService.getUserByUserName(userName);
		if(null == user) {
			throw new UserNotFoundException("Username with " + userName + " does not exist");
		}
		
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
}
