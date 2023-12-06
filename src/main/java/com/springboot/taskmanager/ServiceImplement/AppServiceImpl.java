package com.springboot.taskmanager.ServiceImplement;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.taskmanager.Dto.MessageInfo;
import com.springboot.taskmanager.Dto.UserRegistration;
import com.springboot.taskmanager.ExceptionHandlers.InvalidDataExcaption;
import com.springboot.taskmanager.ExceptionHandlers.UserExistsException;
import com.springboot.taskmanager.Models.Roles;
import com.springboot.taskmanager.Repositories.UserRepository;
import com.springboot.taskmanager.Services.AppService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService{

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<MessageInfo> addUser(UserRegistration userRegistration){
        String message=this.checkUserData(userRegistration);
        if(!message.equals(""))
            throw new InvalidDataExcaption(message);
        try{
            Set<Roles> roles=this.getorCreateRoles(userRegistration.getRoles());
            long cnt=userRepository.countByUsernameOrEmail(userRegistration.getUsername(),userRegistration.getEmail());
            if(cnt!=0)
                throw new UserExistsException("Username or email already in use");
            userRepository.addUser(userRegistration.getUsername(),userRegistration.getEmail() ,passwordEncoder.encode(userRegistration.getPassword()),userRegistration.getFirstName() ,userRegistration.getLastName() ,new Date(System.currentTimeMillis()));
            
            for(Roles role:roles){
                userRepository.addRoles(userRegistration.getUsername(),role.getId());
            }
            return ResponseEntity.ok().body(new MessageInfo("User registration successfull", new Date(System.currentTimeMillis())));
        }
        catch(Exception ex){
            throw new InternalError(ex.getMessage());
        }
    }

    private Set<Roles> getorCreateRoles(Set<String> roleNames){
        Set<Roles> roles=new HashSet<>();
        for(String name:roleNames){
            try{
                roles.add(getRoleObjectByName(name));
            }
            catch(IllegalArgumentException | NullPointerException ex){
                throw new InvalidDataExcaption("Invalid role names is given");
            }
        }
        return roles;
    }
    private Roles getRoleObjectByName(String name){
        if(name.equals("ADMIN")){
            return new Roles(1,name,null);
        }
        else if(name.equals("USER")){
            return new Roles(2,name,null);
        }
        return new Roles(3,"GUEST",null);
    }


    //checks the user data valid or not
    private String checkUserData(UserRegistration user){
        StringBuilder result=new StringBuilder("");
        if(user.getUsername()==null || user.getUsername().equals(""))
            result.append("Email is Mandatory filed$");
        if(user.getEmail()==null || user.getEmail().equals(""))
            result.append("Email is Mandatory filed$");
        if(user.getFirstName()==null || user.getFirstName().equals(""))
            result.append("First Name is missing$");
        if(user.getPassword()==null || user.getPassword().length()<8)
            result.append("Password Field is Missing or length is less then 8$");
        if(user.getRoles()==null || user.getRoles().size()==0)
            result.append("Roles not specified$");
        return result.toString();
    }

}
