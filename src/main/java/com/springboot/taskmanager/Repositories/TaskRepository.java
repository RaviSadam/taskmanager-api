package com.springboot.taskmanager.Repositories;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.taskmanager.Models.Tasks;
import com.springboot.taskmanager.Projections.TaskDetailsProjections;
import com.springboot.taskmanager.Projections.TaskVisibilityProjection;

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
            countQuery = "SELECT COUNT(*) FROM tasks WHERE owner=:username GROUP BY owner",
            nativeQuery=true)
    public List<TaskDetailsProjections> findTasksByUsername(@Param("username") String username,Pageable page);


    @Query(value="SELECT COUNT(*) FROM tasks t " +
           "WHERE t.owner = :username AND t.task_id = :taskId " +
           "GROUP BY t.task_id",nativeQuery=true)
    public int countTasksWithTaskIdAndUsername(@Param("taskId") String taskId,@Param("username") String username);


    @Modifying
    @Transactional
    @Query(value = "UPDATE tasks t " +
            "SET " +
            "t.visibility = COALESCE(:visibility, t.visibility)," +
            "t.updated_date=:date "+
            "WHERE " +
            "t.task_id IN :taskIds AND t.owner = :owner",
            nativeQuery = true)
    public void updateTaskVisibility(
            @Param("taskIds") List<String> taskIds,
            @Param("owner") String username,
            @Param("visibility") String visibility,
            @Param("date") Date date);


    @Modifying
    @Transactional
    @Query(value = "UPDATE tasks t " +
            "SET " +
            "t.visibility = COALESCE(:visibility, t.visibility), " +
            "t.priority = COALESCE(:priority, t.priority), " +
            "t.status = COALESCE(:status, t.status), " +
            "t.description = COALESCE(:description, t.description), " +
            "t.due_date = COALESCE(:duedate, t.due_date)," +
            "t.updated_date=:date "+
            "WHERE " +
            "t.task_id = :taskId AND t.owner = :owner",
            nativeQuery = true)
    public void updateTask(
            @Param("taskId") String taskId,
            @Param("owner") String username,
            @Param("visibility") String visibility,
            @Param("status") String status,
            @Param("priority") String priority,
            @Param("description") String description,
            @Param("duedate") Date dueDate,
            @Param("date") Date date
        );


    //delete tasks by using task ids
    @Modifying
    @Transactional
    @Query(value="DELETE FROM tasks t WHERE t.task_id IN :taskIds AND t.owner=:owner",nativeQuery = true)
    public void deleteTasksByTaskid(@Param("owner")String owner,@Param("taskIds") List<String> taskId);


    @Modifying
    @Transactional
    @Query(value="DELETE tf FROM task_files tf JOIN tasks t on t.task_id=tf.task_id WHERE t.task_id IN :taskIds AND t.owner=:owner",nativeQuery = true)
    public void deleteTaskFilesByTaskId(@Param("owner")String owner,@Param("taskIds") List<String> taskId);


    //delete tasks by username
    @Modifying
    @Transactional
    @Query(value="DELETE FROM tasks t WHERE t.owner=:owner",nativeQuery = true)
    public void deleteTasksByOwner(@Param("owner")String owner);


    @Modifying
    @Transactional
    @Query(value="DELETE tf FROM task_files tf JOIN tasks t on t.task_id=tf.task_id WHERE  t.owner=:owner",nativeQuery = true)
    public void deleteTaskFilesByOwner(@Param("owner")String owner);


    @Query("SELECT t.visibility AS visibility FROM Tasks t WHERE t.taskId IN :taskIds")
    public List<TaskVisibilityProjection> getTasksVisibility(@Param("taskIds") Set<String> taskIds);


    @Query(value="SELECT  t.created_date AS createddate,t.description AS description,t.due_date AS duedate,t.task_name AS taskname,t.priority AS priority,t.status AS status,t.updated_date AS updateddate,t.visibility AS visibility,t.owner AS owner "+
                 "FROM user_tasks_access ut " +
                 "INNER JOIN tasks t " + 
                 "ON t.task_id=ut.task_id " +
                 "WHERE ut.username=:username",
                 countQuery = "SELECT COUNT(*) FROM user_tasks_access ut WHERE ut.username=:username GROUP BY username",
                 nativeQuery = true)
    public List<TaskDetailsProjections> getUserAccessedTasks(@Param("username")String username,Pageable page);
    
    @Modifying
    @Transactional
    @Query(value = "CALL user_tasks_access_table (:usernames,:taskId)", nativeQuery = true)
    public void insertUserTasksAccess(@Param("usernames") String usernames, @Param("taskId") String taskId);


    @Query("SELECT t.createdDate AS createddate,t.description AS description,t.dueDate AS duedate,t.name AS taskname,t.priority AS priority,t.status AS status,t.updatedDate AS updateddate,t.visibility AS visibility,u.username AS owner,t.taskId AS id FROM Tasks t JOIN t.owner u WHERE u.username=:username")
    public List<TaskDetailsProjections> getAllTasksForExporting(@Param("username") String usernames);

    @Query(value="SELECT  t.created_date AS createddate,t.description AS description,t.due_date AS duedate,t.task_name AS taskname,t.priority AS priority,t.status AS status,t.updated_date AS updateddate,t.visibility AS visibility,t.owner AS owner,t.task_id AS id "+
                    "FROM user_tasks_access ut "+
                    "INNER JOIN tasks t  "+
                    "ON t.task_id=ut.task_id "+
                    "WHERE ut.username=:username ", nativeQuery=true)
    public List<TaskDetailsProjections> getAllAccessedTasksForExport(@Param("username")String usrename);

    //updates the task status to EXPIRED
    @Procedure(name = "updateStatusOfTasks",procedureName = "updateStatusOfTasks")
    int updateStatusOfTasks();
}
