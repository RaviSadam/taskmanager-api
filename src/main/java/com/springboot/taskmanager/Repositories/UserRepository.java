package com.springboot.taskmanager.Repositories;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.taskmanager.Models.User;
import com.springboot.taskmanager.Projections.RolesProjection;
import com.springboot.taskmanager.Projections.UserAuthProjection;
import com.springboot.taskmanager.Projections.UserDetailsProjection;
import com.springboot.taskmanager.Projections.UserIdsProjection;

import jakarta.transaction.Transactional;


public interface UserRepository extends JpaRepository<User,java.lang.String> {
    public User findByUsername(String username);

    public long countByUsernameOrEmail(String username,String email);
    
    @Modifying
    @Transactional
    @Query(value="INSERT INTO users (username,email,password,first_name,last_name,created_date) VALUES(:username,:email,:password,:first_name,:last_name,:created_date)",nativeQuery=true)
    public void  addUser(@Param("username") String username,@Param("email")String email,@Param("password") String password,@Param("first_name") String firstName,@Param("last_name") String lastName,@Param("created_date")Date date);


    @Query("SELECT u.username AS username,u.email AS email,u.firstName as firstname,u.lastName AS lastname FROM User u")
    public List<UserIdsProjection> getUserIds(Pageable page);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO user_roles (username,role_id) VALUES(:username,:roleId)",nativeQuery=true)
    public void  addRoles(@Param("username")String username,@Param("roleId")long id);

    @Query("SELECT u.username AS username ,u.password AS password FROM User u WHERE u.username=:username")
    public UserAuthProjection getUserForAuth(@Param("username")String username);

    @Query("SELECT r.rolename AS rolename,r.id AS id FROM User u JOIN u.roles r WHERE u.username=:username")
    public Set<RolesProjection> getUserRoles(@Param("username") String username);

    @Query("SELECT u.username AS username, u.firstName AS firstname, u.lastName AS lastname, u.createdDate AS createddate, u.email AS email, COUNT(t.taskId) AS taskcount " +
            "FROM User u " +
            "JOIN u.tasks t " +
            "WHERE u.username = :username " +
            "GROUP BY u.username")
    public UserDetailsProjection getUserDetails(@Param("username") String username);

    @Query("SELECT u.username AS username, u.firstName AS firstname, u.lastName AS lastname, u.createdDate AS createddate, u.email AS email, COUNT(t.taskId) AS taskcount " +
            "FROM User u " +
            "JOIN u.tasks t " +
            "GROUP BY u.username")
    public List<UserDetailsProjection> getAllUsers(Pageable page);


    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.username=:username")
    public void deleteUser(@Param("username")String username);
}

