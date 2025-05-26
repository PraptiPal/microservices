package com.asti.spring_data_jpa.exceptions;

public class EmployeeNotFoundException extends Exception{
	
	public EmployeeNotFoundException(String message) {
		super(message);
	}

}
