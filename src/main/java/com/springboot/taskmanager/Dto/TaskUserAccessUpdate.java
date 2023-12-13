package com.springboot.taskmanager.Dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskUserAccessUpdate {
    @JsonAlias("usernames")
    private Set<String> usernames;
    @JsonAlias({"taskid","task_id"})
    private String taskIds;
}
