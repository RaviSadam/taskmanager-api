package com.springboot.taskmanager.ExceptionHandlers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserExistsException extends RuntimeException {
    private String message;
    
}
