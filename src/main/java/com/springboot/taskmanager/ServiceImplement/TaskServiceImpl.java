package com.springboot.taskmanager.ServiceImplement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.taskmanager.Dto.DeleteTasksRequest;
import com.springboot.taskmanager.Dto.MessageInfo;
import com.springboot.taskmanager.Dto.TaskDetails;
import com.springboot.taskmanager.Dto.TaskResponse;
import com.springboot.taskmanager.Dto.TaskUserAccessUpdate;
import com.springboot.taskmanager.Dto.UserDataDto;
import com.springboot.taskmanager.Dto.UserIdsDto;
import com.springboot.taskmanager.Dto.UserRegistration;
import com.springboot.taskmanager.ExceptionHandlers.InvalidDataExcaption;
import com.springboot.taskmanager.ExceptionHandlers.NotFoundException;
import com.springboot.taskmanager.Models.Priority;
import com.springboot.taskmanager.Models.Status;
import com.springboot.taskmanager.Models.Visibility;
import com.springboot.taskmanager.Projections.TaskDetailsProjections;
import com.springboot.taskmanager.Projections.TaskVisibilityProjection;
import com.springboot.taskmanager.Projections.UserDetailsProjection;
import com.springboot.taskmanager.Projections.UserIdsProjection;
import com.springboot.taskmanager.Repositories.TaskRepository;
import com.springboot.taskmanager.Repositories.UserRepository;
import com.springboot.taskmanager.Services.TaskService;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{


    @Value("${file.upload-dir}")
    private String fileDirectory;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;




    @Override
    public ResponseEntity<MessageInfo> updateUserTaskAccess(TaskUserAccessUpdate taskUserAccessUpdate){
        List<TaskVisibilityProjection> taskVisibilityProjections=taskRepository.getTasksVisibility(Set.of(taskUserAccessUpdate.getTaskIds()));
        for(TaskVisibilityProjection taskVisibilityProjection:taskVisibilityProjections){
            if(taskVisibilityProjection.getVisibility().equals("PRIVATE"))
                throw new InvalidDataExcaption("Tasks visibility is PRIVATE");
        }
        String usernamesJson = "[\"" + String.join("\", \"", taskUserAccessUpdate.getUsernames()) + "\"]";    
        taskRepository.insertUserTasksAccess(usernamesJson,taskUserAccessUpdate.getTaskIds());
        return ResponseEntity.ok().body(new MessageInfo("Access given to users", this.getDate()));
    }

    @Override
    public ResponseEntity<Set<TaskResponse>> getAccessTasks(int pageNumber,int pageSize){
        // Pageable page=PageRequest.of(pageNumber, pageSize);
        // List<TaskDetailsProjections> taskDetailsProjectionss=taskRepository.getUserAccessedTasks(page);
        // Set<TaskResponse> taskResponses=new HashSet<>();
        // for(TaskDetailsProjections taskDetailsProjections:taskDetailsProjectionss){
        //     taskResponses.add(
        //             TaskResponse.builder()
        //                 .createddate(taskDetailsProjections.getCreateddate())
        //                 .description(taskDetailsProjections.getDescription())
        //                 .duedate(taskDetailsProjections.getDuedate())
        //                 .priority(taskDetailsProjections.getPriority())
        //                 .status(taskDetailsProjections.getStatus())
        //                 .taskname(taskDetailsProjections.getTaskname())
        //                 .updateddate(taskDetailsProjections.getUpdateddate())
        //                 .owner(taskDetailsProjections.getOwner())
        //                 .build()

        //     );
        // }
        return ResponseEntity.ok().body(null);
    }

    //delete task by ids
    @Override
    @Transactional
    public ResponseEntity<MessageInfo> deleteTasks(DeleteTasksRequest deleteTasksRequest){
        taskRepository.deleteTaskFilesByTaskId(this.getLoggedinUserName(),deleteTasksRequest.getTaskIds());
        taskRepository.deleteTasksByTaskid(this.getLoggedinUserName(),deleteTasksRequest.getTaskIds());
        return ResponseEntity.ok().body(new MessageInfo("Tasks deleted", this.getDate()));
    }

    @Override
    @PreAuthorize("#username==principal.username OR hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<MessageInfo> deleteUser(String username){
        long cnt=userRepository.countByUsernameOrEmail(username,username);
        if(cnt!=1)
            throw new UsernameNotFoundException("User not found");
        taskRepository.deleteTaskFilesByOwner(username);
        System.out.println("Task Files deleted");
        taskRepository.deleteTasksByOwner(username);
        System.out.println("User tasks deleted");
        userRepository.deleteUser(username);
        return ResponseEntity.ok().body(new MessageInfo("User deleted",this.getDate()));
    }


    @Override
    public ResponseEntity<MessageInfo> updateUser(String username,UserRegistration userRegistration){

        return null;
    }


    @Override
    public ResponseEntity<MessageInfo> updateTask(String taskId, TaskDetails taskDetails){
        String visibility=null,status=null,priority=null;
        if(taskDetails.getVisibility()!=null){
            if(taskDetails.getVisibility()==0)
                visibility=Visibility.PRIVATE.toString();
            else if(taskDetails.getVisibility()==1)
                visibility=Visibility.PUBLIC.toString();
            else
                visibility=Visibility.SPECIFIC.name();
        }
        if(taskDetails.getPriority()!=null){
            if(taskDetails.getPriority()<=5)
                priority=Priority.LOW.toString();
            else if(taskDetails.getPriority()<=8)
                priority=Priority.MEDIUM.toString();
            else
                priority=Priority.HIGH.toString();
        }
        if(taskDetails.getStatus()!=null){
            if(taskDetails.getStatus()==0)
                status=Status.NOT_COMPLETED.toString();
            else if(taskDetails.getStatus()==1)
                status=Status.COMPLETED.toString();
            else
                status=Status.EXPIRED.name();
        }
        Date date=null;
        if(taskDetails.getDueDate()!=null)
            date=new Date(taskDetails.getDueDate().getTime());

        taskRepository.updateTask(taskId, this.getLoggedinUserName(), visibility, status, priority, taskDetails.getDescription(), date,this.getDate());
        return ResponseEntity.ok().body(new MessageInfo("Task Updated", new java.util.Date(System.currentTimeMillis())));
    }


    @Override
    public ResponseEntity<MessageInfo> updateVisibility(DeleteTasksRequest deleteTasksRequest, int visibi){
        String visibility=null;
        if(visibi==0)
            visibility=Visibility.PRIVATE.name();
        else if(visibi==1)
            visibility=Visibility.PUBLIC.name();
        else
            visibility=Visibility.SPECIFIC.name();
        try{    
            System.out.println("ok3");
            taskRepository.updateTaskVisibility(deleteTasksRequest.getTaskIds(),this.getLoggedinUserName(),visibility,this.getDate());
            System.out.println("ok4");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return ResponseEntity.ok().body(new MessageInfo("Task Visibility chnaged", new Date(System.currentTimeMillis())));
    }

    @Override
    @RolesAllowed({"ADMIN"})
    public Set<UserDataDto> getUserAllUserDetails(int pageNumber,int pageSize){
        Pageable page=PageRequest.of(pageNumber, pageSize);
        List<UserDetailsProjection> userDetailsProjections=userRepository.getAllUsers(page);
        Set<UserDataDto> userDataDtos=new HashSet<>();
        for(UserDetailsProjection userDetailsProjection:userDetailsProjections){
                userDataDtos.add(
                    UserDataDto.builder()
                        .createdDate(userDetailsProjection.getCreateddate())
                        .email(userDetailsProjection.getEmail())
                        .firstName(userDetailsProjection.getFirstname())
                        .lastName(userDetailsProjection.getLastname())
                        .taskCount(userDetailsProjection.getTaskcount())
                        .username(userDetailsProjection.getUsername())
                        .build()
                    );

        }
        return userDataDtos;
    }

    @Override
    public ResponseEntity<UserDataDto> getUserDetails(String username){
        if(username==null || username.equals("")){
            username=this.getLoggedinUserName();
        }
        UserDetailsProjection userDetailsProjection =userRepository.getUserDetails(username);
        if(userDetailsProjection==null)
            throw new NotFoundException("User not found with username:"+username);
        return ResponseEntity.ok().body(
                    UserDataDto.builder()
                        .createdDate(userDetailsProjection.getCreateddate())
                        .email(userDetailsProjection.getEmail())
                        .firstName(userDetailsProjection.getFirstname())
                        .lastName(userDetailsProjection.getLastname())
                        .taskCount(userDetailsProjection.getTaskcount())
                        .username(userDetailsProjection.getUsername())
                        .build()
                );
    }


    @Override
    @RolesAllowed({"USER","ADMIN"})
    public Set<TaskResponse> getTasks(int pageNumber, int pageSize, String sort) {
        Pageable page=PageRequest.of(pageNumber, pageSize);
        List<TaskDetailsProjections> taskDetailsProjectionss=taskRepository.findTasksByUsername(this.getLoggedinUserName(),page);
        Set<TaskResponse> result=new HashSet<>();
        for(TaskDetailsProjections taskDetailsProjections:taskDetailsProjectionss){
            result.add(
                TaskResponse.builder()
                        .createddate(taskDetailsProjections.getCreateddate())
                        .description(taskDetailsProjections.getDescription())
                        .duedate(taskDetailsProjections.getDuedate())
                        .priority(taskDetailsProjections.getPriority())
                        .status(taskDetailsProjections.getStatus())
                        .taskname(taskDetailsProjections.getTaskname())
                        .updateddate(taskDetailsProjections.getUpdateddate())
                        .owner(taskDetailsProjections.getOwner())
                        .build()
            );
        }
        return result;
    }
    @Override
    public ResponseEntity<Set<UserIdsDto>> getUserIds(int pageNumber,int pageSize){
        Set<UserIdsDto> ids=new HashSet<>();
        Pageable page=PageRequest.of(pageNumber, pageSize);
        for(UserIdsProjection userIdsProjection:userRepository.getUserIds(page)){
            ids.add(
                UserIdsDto.builder()
                    .email(userIdsProjection.getEmail())
                    .firstname(userIdsProjection.getFirstname())
                    .lastname(userIdsProjection.getLastname())
                    .username(userIdsProjection.getusername())
                    .build()
            );
        }
        return ResponseEntity.ok().body(ids);
    }


    @Override
    @PostAuthorize("returnObject.owner == principal.username")
    public TaskResponse getTaskDetails(String taskId){
        TaskDetailsProjections taskDetailsProjections=taskRepository.findTaskById(taskId);
        if(taskDetailsProjections==null)
            throw new NotFoundException("Task Not found");
        return TaskResponse.builder()
                    .createddate(taskDetailsProjections.getCreateddate())
                    .description(taskDetailsProjections.getDescription())
                    .duedate(taskDetailsProjections.getDuedate())
                    .priority(taskDetailsProjections.getPriority())
                    .status(taskDetailsProjections.getStatus())
                    .taskname(taskDetailsProjections.getTaskname())
                    .updateddate(taskDetailsProjections.getUpdateddate())
                    .owner(taskDetailsProjections.getOwner())
                    .build();
    }



    @Override
    public ResponseEntity<MessageInfo> addTask(TaskDetails taskDetails){
        // MultipartFile multipartFile=null;
        String taskId="Task-"+this.getUuid(),username=this.getLoggedinUserName();
        
        //setting proority
        String priority=null;
        if(taskDetails.getPriority()<=5)
            priority=Priority.LOW.toString();
        else if(taskDetails.getPriority()<=8)
            priority=Priority.MEDIUM.toString();
        else
            priority=Priority.HIGH.toString();
        
        //setting visibility
        String visibility=null;
        if(taskDetails.getVisibility()==0)
            visibility=Visibility.PUBLIC.name();
        else if(taskDetails.getVisibility()==1)
            visibility=Visibility.PRIVATE.name();
        else
            visibility=Visibility.SPECIFIC.name();
        try{
            // if(multipartFile!=null){
            //     String fileName=this.fileProcess(multipartFile);
            //     //saving file name in DB
            //     taskRepository.addTaskAttachments(taskId, fileName);
            // }
            //adding task data to DB
            taskRepository.addTask(taskId, new Date(System.currentTimeMillis()), taskDetails.getDescription(), new Date(taskDetails.getDueDate().getTime()), taskDetails.getTaskName(), priority, taskDetails.isSendNotifications(), Status.NOT_COMPLETED.toString(), new Date(System.currentTimeMillis()), visibility, username);
        }
        catch(Exception ex){}
        return ResponseEntity.ok().body(new MessageInfo("Task added", new java.util.Date(System.currentTimeMillis())));
    }
    
    //give random file name and moves file to static folder
    public String fileProcess(MultipartFile multipartFile){
        Path path=null;
        try {
            String fileName=this.getUuid();
            String fileExtension=null;
            String filename=multipartFile.getOriginalFilename();
            if(filename!=null)
                fileExtension=filename.split(".")[1];
            path=Path.of(fileDirectory+fileName+"."+fileExtension);
            multipartFile.transferTo(path);
            return fileName+"."+fileExtension;
        } catch (IllegalStateException | IOException |NullPointerException e) {
            if(path!=null){
                File file=path.toFile();
                file.deleteOnExit();    
            }
        }
        return null;
    }

    //return random uuid as string
    private String getUuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    //return logged in username
    private String getLoggedinUserName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    private Date getDate(){
        return new Date(System.currentTimeMillis());
    }
}
