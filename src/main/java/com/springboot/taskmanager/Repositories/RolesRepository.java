package com.springboot.taskmanager.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.taskmanager.Models.Roles;

public interface RolesRepository  extends JpaRepository<Roles,Long>{
    public Optional<Roles> getReferenceByRolename(String name);  
}
