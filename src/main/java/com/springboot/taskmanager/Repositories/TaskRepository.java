package com.springboot.taskmanager.Repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.taskmanager.Models.Tasks;
import com.springboot.taskmanager.Projections.TaskDetailsProjections;

import jakarta.transaction.Transactional;

public interface TaskRepository extends JpaRepository<Tasks,String>{

    //adding task to DB
    @Modifying
    @Transactional
    @Query(value="INSERT INTO tasks (task_id,created_date,description,due_date,task_name,priority,send_notifications,status,updated_date,visibility,owner)" +
            "VALUES(:taskId,:createdDate,:description,:dueDate,:taskName,:priority,:sendNofications,:status,:updatedDate,:vissibility,:owner)",nativeQuery = true)
    public void addTask(@Param("taskId")String taskId,@Param("createdDate")Date createdDate,
                        @Param("description")String description,@Param("dueDate")Date dueDate,
                        @Param("taskName")String taskName,@Param("priority")String priority,
                        @Param("sendNofications")boolean notifications,@Param("status")String status,
                        @Param("updatedDate")Date updatedDate,@Param("vissibility")String visibility,
                        @Param("owner")String username
                );
    //adding task file name to db
    @Modifying
    @Transactional
    @Query(value="INSERT INTO task_files (task_id,attachments) VALUES(:taskId,:file)",nativeQuery = true)
    public void addTaskAttachments(@Param("taskId") String taskId,@Param("file")String file);


    //retriving task details
    @Query(value ="SELECT created_date AS createddate,description AS description,due_date AS duedate,task_name AS taskname,priority AS priority,status AS status,updated_date AS updateddate,visibility AS visibility,owner AS owner FROM tasks WHERE task_id=:taskId",nativeQuery=true)
    public TaskDetailsProjections findTaskById(@Param("taskId")String taskId);


    @Query(value ="SELECT created_date AS createddate,description AS description,due_date AS duedate,task_name AS taskname,priority AS priority,status AS status,updated_date AS updateddate,visibility AS visibility,owner AS owner FROM tasks WHERE owner=:username",
            countQuery = "SELECT COUNT(*) FROM tasks WHERE owner=:username",
            nativeQuery=true)
    public List<TaskDetailsProjections> findTasksByUsername(@Param("username") String username,Pageable page);


    @Query(value="SELECT COUNT(*) FROM task_manager.tasks t" + 
                  "WHERE t.owner=:username AND t.task_id=:taskId" + 
                  "GROUP BY t.task_id",
                nativeQuery = true)
    public int countTasksWithTaskIdAndUsername(@Param("taskId") String taskId,@Param("username") String username);


    @Modifying
    @Transactional
    @Query(value="UPDATE Tasks t" +
                "SET"+
                "t.visibility = IF(:visibility IS NULL, t.visibility, :visibility),"+
                "t.updatedDate=:date"+
                "WHERE"+
                "t.taskId = :taskId AND t.owner = :owner;")
    
    public void updateTaskVisibility(@Param("taskId") String taskId,@Param("owner") String username,@Param("visibility") String visibility,@Param("date")Date date);



    @Modifying
    @Transactional
    @Query(value="UPDATE Tasks t" +
                "SET"+
                "t.visibility = IF(:visibility IS NULL, t.visibility, :visibility),"+
                "t.priority = IF(:priority IS NULl , t.priority,:priority)"+
                "t.status = IF(:staus IS NULL,t.staus,:status)"+
                "t.description=IF(:description IS NULL, t.description,:description)"+
                "t.dueDate = IF(:duedate IS NULL,t.dueDate,:duedate)"+
                "t.updatedDate=:date"+
                "WHERE"+
                "t.taskId = :taskId AND t.owner = :owner;")
    
    public void updateTask(@Param("taskId") String taskId,@Param("owner") String username,@Param("visibility") String visibility,@Param("status") String status,@Param("priority") String priority,@Param("description")String description,@Param("duedate")Date dueDate,@Param("date")Date date);

}
