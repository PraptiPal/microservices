package com.asti.spring_data_jpa.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asti.spring_data_jpa.entity.Employee;
import com.asti.spring_data_jpa.exceptions.EmployeeAlreadyExistsException;
import com.asti.spring_data_jpa.exceptions.EmployeeNotFoundException;
import com.asti.spring_data_jpa.repository.EmployeeRepository;
import com.asti.spring_data_jpa.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{

	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	@Override
	public Employee createEmployee(Employee employee) throws EmployeeAlreadyExistsException{
		
		List<Employee> employeesDb = employeeRepository.findByName(employee.getName());
		
		if(employeesDb != null && !employeesDb.isEmpty()) {
			throw new EmployeeAlreadyExistsException("Employee with name '" + employee.getName() + "' already exists");
		}
		
		Employee employeeDb = employeeRepository.save(employee);
		
		return employeeDb;
	}


	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		return employees;
	}


	@Override
	public Employee getEmployee(Long empId) throws EmployeeNotFoundException{
		
		Optional<Employee> employee = employeeRepository.findById(empId);
		
		if(!employee.isPresent())
			throw new EmployeeNotFoundException("Employee with id '" +empId + "' does not exist");
		
		return employee.get();
	}


	@Override
	public boolean deletetEmployee(Long empId) {
		employeeRepository.deleteById(empId);
		return true;
	}


	@Override
	public Employee updateEmployee(Long empId, Employee employee) {
		
		employee.setEmpId(empId);
		
		Employee employeeDb = employeeRepository.save(employee);
		
		return employeeDb;
	}


}
