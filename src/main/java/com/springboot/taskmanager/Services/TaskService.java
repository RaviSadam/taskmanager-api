package com.springboot.taskmanager.Services;

import java.util.Set;

import org.springframework.http.ResponseEntity;

import com.springboot.taskmanager.Dto.MessageInfo;
import com.springboot.taskmanager.Dto.TaskDetails;
import com.springboot.taskmanager.Dto.TaskResponse;
import com.springboot.taskmanager.Dto.UserDataDto;

public interface TaskService {

    //addes new task to db
    public ResponseEntity<MessageInfo> addTask(TaskDetails taskDetails);

    //gives the details of task based on task id
    public TaskResponse  getTaskDetails(String taskId);

    public Set<TaskResponse> getTasks(int pageNumber, int pageSize, String sort);

    //gives user details or currently loggedin user details
    public ResponseEntity<UserDataDto> getUserDetails(String username);

    //get users details
    public Set<UserDataDto> getUserAllUserDetails(int pageNumber,int pageSize);

    //updates the visibility
    public ResponseEntity<MessageInfo> updateVisibility(String taskId, int visibility);

    //update status and priority
    public ResponseEntity<MessageInfo> updateTask(String taskId,TaskDetails taskDetails);
}
