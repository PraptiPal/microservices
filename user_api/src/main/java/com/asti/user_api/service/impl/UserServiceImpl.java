package com.asti.user_api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.asti.user_api.entity.User;
import com.asti.user_api.exception.UserAlreadyExistsException;
import com.asti.user_api.repository.UserRepository;
import com.asti.user_api.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public User createUser(User userInput) throws UserAlreadyExistsException {
		
		User userDb = userRepository.findByUserName(userInput.getUserName());
		
		if(null != userDb) {
			throw new UserAlreadyExistsException("User with name " + userInput.getUserName() + " already exists");
		}
			
		User newUser = userRepository.save(userInput);
		return newUser;
	}

	@Override
	public User getUserByUserName(String userName) {
		User userDb = userRepository.findByUserName(userName);
		return userDb;
	}

}
