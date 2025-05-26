package com.asti.user_api.service;

import com.asti.user_api.entity.User;
import com.asti.user_api.exception.UserAlreadyExistsException;

public interface UserService {

	User createUser(User user) throws UserAlreadyExistsException;
	
	User getUserByUserName(String userName);
}
