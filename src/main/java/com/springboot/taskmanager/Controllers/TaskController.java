package com.springboot.taskmanager.Controllers;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.taskmanager.Dto.DeleteTasksRequest;
import com.springboot.taskmanager.Dto.MessageInfo;
import com.springboot.taskmanager.Dto.TaskDetails;
import com.springboot.taskmanager.Dto.TaskResponse;
import com.springboot.taskmanager.Dto.TaskUserAccessUpdate;
import com.springboot.taskmanager.Dto.UserDataDto;
import com.springboot.taskmanager.Dto.UserIdsDto;
import com.springboot.taskmanager.Services.TaskService;

import lombok.RequiredArgsConstructor;


// org.springframework.web.servlet.resource.NoResourceFoundException:
// org.springframework.web.HttpRequestMethodNotSupportedException:

@Controller
@ResponseBody
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/get-task-details/{taskId}")
    public ResponseEntity<TaskResponse> getTaskDetails(@PathVariable("taskId")String taskId){
        return ResponseEntity.ok().body(taskService.getTaskDetails(taskId));
    }

    @GetMapping("/get-tasks")
    public ResponseEntity<Set<TaskResponse>> getTasks(
        @RequestParam(value="pageNumber",required = false,defaultValue = "0") int pageNumber,
        @RequestParam(value="pageSize",required = false,defaultValue = "10")int pageSize,
        @RequestParam(value="sort",required = false,defaultValue = "priority")String sort
        ){
            return ResponseEntity.ok().body(taskService.getTasks(pageNumber,pageSize,sort));  
    }

    @GetMapping("/get-user")
    public ResponseEntity<UserDataDto> getUser(@RequestParam(value="username",required = false)String username){
        return taskService.getUserDetails(username);
    }
    @GetMapping("/get-user-ids")
    public ResponseEntity<Set<UserIdsDto>> getUserIds(
        @RequestParam(value="pageNumber",required = false,defaultValue = "0") int pageNumber,
        @RequestParam(value="pageSize",required = false,defaultValue = "10")int pageSize
    ){
        return  taskService.getUserIds(pageNumber, pageSize);
    }
    @GetMapping("/get-users")
    public ResponseEntity<Set<UserDataDto>> getUsers(
        @RequestParam(value="pageNumber",required = false,defaultValue = "0") int pageNumber,
        @RequestParam(value="pageSize",required = false,defaultValue = "10")int pageSize){
        return ResponseEntity.ok().body(taskService.getUserAllUserDetails(pageNumber,pageSize));
    }


    @GetMapping("/get-assess-tasks")
    public ResponseEntity<Set<TaskResponse>> getAccessTasks(
        @RequestParam(value="pageNumber",required = false,defaultValue = "0") int pageNumber,
        @RequestParam(value="pageSize",required = false,defaultValue = "10")int pageSize
    ){
        return taskService.getAccessTasks(pageNumber, pageSize);
    }

    @PostMapping("/add-task")
    public ResponseEntity<MessageInfo> addTask(@RequestBody TaskDetails taskDetails){
        return taskService.addTask(taskDetails);
    }

    @PutMapping("/update-visibility")
    public ResponseEntity<MessageInfo> updateVisibility(@RequestBody DeleteTasksRequest deleteTasksRequest,@RequestParam(value="visibility",required = false,defaultValue = "2")int visibility){
        return taskService.updateVisibility(deleteTasksRequest,visibility);
    }

    @PutMapping("/update-task/{taskId}")
    public ResponseEntity<MessageInfo> updateTask(@PathVariable("taskId") String taskId,@RequestBody TaskDetails taskDetails){
        return taskService.updateTask(taskId,taskDetails);
    }
    @PutMapping("/update-access")
    public ResponseEntity<MessageInfo> updateUserTaskAccess(@RequestBody TaskUserAccessUpdate taskUserAccessUpdate){
        return taskService.updateUserTaskAccess(taskUserAccessUpdate);
    }
    
    @DeleteMapping("/delete-user/{username}")
    public ResponseEntity<MessageInfo> deleteUser(@PathVariable("username")String username){
        return taskService.deleteUser(username);
    }

    @DeleteMapping("/delete-task")
    public ResponseEntity<MessageInfo> deleteTask(@RequestBody DeleteTasksRequest tasksIds){
        return taskService.deleteTasks(tasksIds);
    }

}
