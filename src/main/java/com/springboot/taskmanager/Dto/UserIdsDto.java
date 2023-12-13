package com.springboot.taskmanager.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserIdsDto {
    private String email;
    private String username;
    private String firstname;
    private String lastname;
}
