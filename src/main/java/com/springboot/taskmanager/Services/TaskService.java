package com.springboot.taskmanager.Services;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.taskmanager.Dto.DeleteTasksRequest;
import com.springboot.taskmanager.Dto.MessageInfo;
import com.springboot.taskmanager.Dto.TaskDetails;
import com.springboot.taskmanager.Dto.TaskResponse;
import com.springboot.taskmanager.Dto.TaskUserAccessUpdate;
import com.springboot.taskmanager.Dto.UserDataDto;
import com.springboot.taskmanager.Dto.UserIdsDto;
import com.springboot.taskmanager.Dto.UserRegistration;

import jakarta.servlet.http.HttpServletResponse;

public interface TaskService {

    //addes new task to db
    public ResponseEntity<MessageInfo> addTask(TaskDetails taskDetails,MultipartFile file);

    //gives the details of task based on task id
    public TaskResponse  getTaskDetails(String taskId);

    public Set<TaskResponse> getTasks(int pageNumber, int pageSize, String sort);

    //gives user details or currently loggedin user details
    public ResponseEntity<UserDataDto> getUserDetails(String username);

    //get users details
    public Set<UserDataDto> getUserAllUserDetails(int pageNumber,int pageSize);

    //updates the visibility
    public ResponseEntity<MessageInfo> updateVisibility(DeleteTasksRequest deleteTasksRequest, int visibility);

    //updates the status of task
    public ResponseEntity<MessageInfo> updateStatus(DeleteTasksRequest deleteTasksRequest, int status);

    //update task details
    public ResponseEntity<MessageInfo> updateTask(String taskId,TaskDetails taskDetails);

    //update user details
    public ResponseEntity<MessageInfo> updateUser(String username,UserRegistration userRegistration);

    //deletes user and corresponding user tasks and files
    public ResponseEntity<MessageInfo> deleteUser(String username);

    //delete tasks by user name
    public ResponseEntity<MessageInfo> deleteTasks(DeleteTasksRequest tasksIds);

    //returns the user ids with size
    public ResponseEntity<Set<UserIdsDto>> getUserIds(int pageNumber,int pageSize);

    //returns the user others task details whose he has access
    public ResponseEntity<Set<TaskResponse>> getAccessTasks(int pageNumber,int pageSize);

    //gives the access task access to users
    public ResponseEntity<MessageInfo> updateUserTaskAccess(TaskUserAccessUpdate taskUserAccessUpdate);

    //give tasks as excel sheet
    public void getExcelSheetOfTasks(HttpServletResponse response);

    //return user name
    public String getLoggedinUserName();

    
}
