package com.springboot.taskmanager.Projections;

import java.sql.Date;

public interface TaskDetailsProjections {
    String getTaskname();
    Date getCreateddate();
    Date getUpdateddate();
    Date getDuedate();
    String getDescription();
    String getPriority();
    String getStatus();
    String getOwner();
}
