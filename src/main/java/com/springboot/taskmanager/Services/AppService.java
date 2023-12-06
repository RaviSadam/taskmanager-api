package com.springboot.taskmanager.Services;

import org.springframework.http.ResponseEntity;

import com.springboot.taskmanager.Dto.MessageInfo;
import com.springboot.taskmanager.Dto.UserRegistration;


public interface AppService {
     public ResponseEntity<MessageInfo> addUser(UserRegistration userRegistration);
}
