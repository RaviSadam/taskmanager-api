package com.springboot.taskmanager.UserServiceImplement;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.taskmanager.Models.User;
import com.springboot.taskmanager.Projections.RolesProjection;
import com.springboot.taskmanager.Projections.UserAuthProjection;
import com.springboot.taskmanager.Repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAuthProjection userProjection=userRepository.getUserForAuth(username);
        if(userProjection==null)
            throw new UsernameNotFoundException("user not found");
        Set<RolesProjection> roles=userRepository.getUserRoles(username);
        User user=new User();
        user.setPassword(userProjection.getPassword());
        user.setUsername(username);

        Set<SimpleGrantedAuthority> authorities=new HashSet<>();
        for(RolesProjection rolesProjection:roles){
            authorities.add(new SimpleGrantedAuthority(rolesProjection.getRolename()));
        }
        user.setAuthorities(authorities);
        return user;
    } 
}
