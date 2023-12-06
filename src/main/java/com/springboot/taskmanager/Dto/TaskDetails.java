package com.springboot.taskmanager.Dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDetails {
    @JsonAlias({"taskname","task_name"})
    private String taskName;
    @JsonAlias({"duedate","due_date"})
    private Date dueDate;

    private String description;
    private Integer priority;
    @JsonAlias({"sendnotifications","send_notifications"})
    private boolean sendNotifications;
    private Integer status;
    private Integer visibility;
}
