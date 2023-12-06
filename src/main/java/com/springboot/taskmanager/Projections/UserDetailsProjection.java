package com.springboot.taskmanager.Projections;

import java.sql.Date;

public interface UserDetailsProjection {
    String getUsername();
    String getEmail();
    String getFirstname();
    String getLastname();
    Date getCreateddate();
    long getTaskcount();
}
