package com.asti.spring_data_jpa.service;

import java.util.List;

import com.asti.spring_data_jpa.entity.Employee;
import com.asti.spring_data_jpa.exceptions.EmployeeAlreadyExistsException;
import com.asti.spring_data_jpa.exceptions.EmployeeNotFoundException;

public interface EmployeeService {

	public Employee createEmployee(Employee employee) throws EmployeeAlreadyExistsException;
	
	 List<Employee> getAllEmployees();
	 
	 Employee getEmployee(Long empId) throws EmployeeNotFoundException;
	 
	 boolean deletetEmployee(Long empId);
	 
	 public Employee updateEmployee(Long empId, Employee employee);
}
