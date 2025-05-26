package com.asti.spring_data_jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asti.spring_data_jpa.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	public List<Employee> findByName(String name);
}
