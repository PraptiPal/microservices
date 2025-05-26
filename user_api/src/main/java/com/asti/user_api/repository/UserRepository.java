package com.asti.user_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asti.user_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByUserName(String userName);
}
