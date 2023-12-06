package com.springboot.taskmanager.ExceptionHandlers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NotFoundException extends RuntimeException {
    private String message;
}
