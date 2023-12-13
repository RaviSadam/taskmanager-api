package com.springboot.taskmanager.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteTasksRequest {
    @JsonAlias({"taskids","task_ids"})
    List<String> taskIds;
}
