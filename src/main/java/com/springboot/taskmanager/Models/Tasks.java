package com.springboot.taskmanager.Models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tasks {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "task_id", columnDefinition = "VARCHAR(255)")
    private String taskId;

    @Column(name="task_name",nullable = false,length = 70)
    private String name;

    @Column(length = 512)
    private String description;

    @Column(name="created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;

    @Column(name="updated_date")
    @Temporal(TemporalType.DATE)
    private Date updatedDate;

    @Column(name="due_date",nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name="priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name="visibility")
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(name="send_notifications")
    private boolean sendNotifications;

    @ElementCollection
    @CollectionTable(
        name="task_files",
        joinColumns = {
            @JoinColumn(name="task_id",referencedColumnName = "task_id")
        }
    )
    private Set<String> attachments=new HashSet<>();

    //task owner
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "owner",referencedColumnName = "username")
    private User owner;

    //assess to specific people
    @OneToMany(mappedBy = "tasksAccess",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<User> accessTo=new HashSet<>();
    

}
