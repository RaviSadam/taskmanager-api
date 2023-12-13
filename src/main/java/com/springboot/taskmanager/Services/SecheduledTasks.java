package com.springboot.taskmanager.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.springboot.taskmanager.Repositories.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@EnableAsync
public class SecheduledTasks {
    @Autowired
    TaskRepository taskRepository;
    @Scheduled(cron="0 0 0 * * ?")
    @Async
    public void function(){
        int val=taskRepository.updateStatusOfTasks();
        System.out.println("Today Number of tasks Expired"+val);
    }
}
