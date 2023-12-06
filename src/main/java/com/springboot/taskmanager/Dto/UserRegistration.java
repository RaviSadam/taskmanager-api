package com.springboot.taskmanager.Dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {
    private String username;
    private String email;
    private String password;

    @JsonAlias({"firstname","first_name"})
    private String firstName;
    @JsonAlias({"last_name","lastname"})
    private String lastName;

    private Set<String> roles;    
}
