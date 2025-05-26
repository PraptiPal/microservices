package com.icress.emp_api.service.impl;

import com.icress.emp_api.entity.Employee;
import com.icress.emp_api.exception.EmployeeAlreadyExistsException;
import com.icress.emp_api.repository.EmployeeRepository;
import com.icress.emp_api.service.EmployeeService;

import java.util.List;
import java.util.Optional;

public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee){

        Optional<Employee> employeeDb = employeeRepository.findByName(employee.getName());

        if(employeeDb.isPresent()){
            throw new EmployeeAlreadyExistsException("Employee already exists with the name " + employee.getName());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long empId){
        return employeeRepository.findById(empId);
    }

    @Override
    public void deletetEmployee(Long empId) {
        employeeRepository.deleteById(empId);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}
