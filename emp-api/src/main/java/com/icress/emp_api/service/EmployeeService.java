package com.icress.emp_api.service;

import com.icress.emp_api.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee createEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(Long empId);

    void deletetEmployee(Long empId);

    Employee updateEmployee(Employee employee);
}
