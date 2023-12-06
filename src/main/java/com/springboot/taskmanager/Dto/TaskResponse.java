package com.springboot.taskmanager.Dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {
    String taskname;
    Date createddate;
    Date updateddate;
    Date duedate;
    String description;
    String priority;
    String status;
    String owner;
}
