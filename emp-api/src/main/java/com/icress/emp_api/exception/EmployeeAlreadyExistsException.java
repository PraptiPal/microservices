package com.icress.emp_api.exception;

public class EmployeeAlreadyExistsException extends RuntimeException{

    private static final long serialVersionUID = 1;

    public EmployeeAlreadyExistsException(String message){
        super(message);
    }

    public EmployeeAlreadyExistsException(String message, Throwable cause){
        super(message, cause);
    }
}
