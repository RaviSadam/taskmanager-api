package com.springboot.taskmanager.ExceptionHandlers;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.springboot.taskmanager.Dto.MessageInfo;

@ControllerAdvice
@ResponseBody
public class GlobalHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<MessageInfo> methodNotFoun(HttpRequestMethodNotSupportedException invalidDataExcaption){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageInfo("Requested service not found", new Date(System.currentTimeMillis())));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<MessageInfo> resourceNotFoun(NoResourceFoundException noResourceFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageInfo("Not found", new Date(System.currentTimeMillis())));
    }
    
    @ExceptionHandler(InvalidDataExcaption.class)
    public ResponseEntity<MessageInfo> invalidDataException(InvalidDataExcaption invalidDataExcaption){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageInfo(invalidDataExcaption.getMessage(), new Date(System.currentTimeMillis())));
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageInfo> notFoundException(NotFoundException notFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageInfo(notFoundException.getMessage(), new Date(System.currentTimeMillis())));
    }
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<MessageInfo> userExistsException(UserExistsException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageInfo(ex.getMessage(), new Date(System.currentTimeMillis())));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageInfo> exception(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageInfo(ex.getMessage(), new Date(System.currentTimeMillis())));
    }
}
