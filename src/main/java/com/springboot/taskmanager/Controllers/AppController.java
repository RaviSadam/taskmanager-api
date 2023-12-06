package com.springboot.taskmanager.Controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springboot.taskmanager.Dto.LoginRequest;
import com.springboot.taskmanager.Dto.LoginResponse;
import com.springboot.taskmanager.Dto.MessageInfo;
import com.springboot.taskmanager.Dto.UserRegistration;
import com.springboot.taskmanager.Services.AppService;
import com.springboot.taskmanager.UserServiceImplement.JwtService;

import lombok.RequiredArgsConstructor;

@Controller
@ResponseBody
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppService appService;


    @PostMapping("/login")
    //authenticating user and generating jwt token
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        if(authentication.isAuthenticated()){
            //this we generate jwt token and return response
            Set<SimpleGrantedAuthority> authorities=new HashSet<>();
            for(GrantedAuthority aGrantedAuthority:authentication.getAuthorities()){
                authorities.add(new SimpleGrantedAuthority(aGrantedAuthority.getAuthority()));
            }
            return new LoginResponse(loginRequest.getUsername(),jwtService.generateToken(loginRequest.getUsername(),authorities));
        }
        throw new UsernameNotFoundException("Invalid user request!");
    }
    @PostMapping("/add-user")
    public ResponseEntity<MessageInfo> addUser(@RequestBody UserRegistration userRegistration){
        return appService.addUser(userRegistration);
    }
}
