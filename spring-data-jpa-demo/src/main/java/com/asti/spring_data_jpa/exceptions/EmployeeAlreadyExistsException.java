package com.asti.spring_data_jpa.exceptions;

public class EmployeeAlreadyExistsException extends Exception{
	
	public EmployeeAlreadyExistsException(String message) {
		super(message);
	}
}
