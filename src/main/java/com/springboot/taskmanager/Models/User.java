package com.springboot.taskmanager.Models;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails{
    @Id
    @Column(name="username",length = 50,unique = true,nullable = false)
    private String username;

    @Column(nullable = false,unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @Column(name="created_date")
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    

    @Column(name="first_name",nullable = false,length = 70)
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    //user roles
    @ManyToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER)
    @JoinTable(
        name="user_roles",
        joinColumns = {
            @JoinColumn(
                name="username",
                referencedColumnName = "username"
            )
        },
        inverseJoinColumns = {
            @JoinColumn(
                name="role_id",
                referencedColumnName ="role_id"
            )
        }
    )
    private Set<Roles> roles;

    //user tasks
    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Tasks> tasks;
    
    //access to another tasks
    @ManyToMany(mappedBy = "accessTo" ,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Tasks> tasksAccess;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getSimpleGrantedAuthorities();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
    public Set<SimpleGrantedAuthority> getSimpleGrantedAuthorities(){
        return authorities;
    }
    @Transient
    Set<SimpleGrantedAuthority> authorities;
}
