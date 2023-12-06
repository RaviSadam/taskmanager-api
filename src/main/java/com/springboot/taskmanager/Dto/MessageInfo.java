package com.springboot.taskmanager.Dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageInfo {
    private String message;
    private Date date;
    
}
