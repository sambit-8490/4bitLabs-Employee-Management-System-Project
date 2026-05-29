package com.fourbitlabs.employee_management_system.exception;

public class BadCredentialsException extends RuntimeException{
    public BadCredentialsException(String message) {
        super(message);
    }
}
