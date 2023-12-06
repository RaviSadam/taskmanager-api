package com.springboot.taskmanager.Models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Roles {
    @Id
    @Column(name="role_id")
    private long id;

    @Column(name="role_name",length = 40,nullable =false,unique = true)
    private String rolename;

    @ManyToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        mappedBy = "roles"
    )
    private Set<User> users=new HashSet<>();
}
