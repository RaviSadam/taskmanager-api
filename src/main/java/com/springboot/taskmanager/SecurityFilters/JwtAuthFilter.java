package com.springboot.taskmanager.SecurityFilters;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.springboot.taskmanager.Models.User;
import com.springboot.taskmanager.UserServiceImplement.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHead=request.getHeader("Authorization");
        String token=null;
        String username=null;
        if(authHead!=null && authHead.startsWith("Bearer ")){
            token=authHead.substring(7);
            username=jwtService.extractUsername(token); 
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user=User.builder().username(username).authorities(jwtService.extractRoles(token)).build();
            UserPrincipalAuthenticationToken userPrincipalAuthenticationToken=new UserPrincipalAuthenticationToken(user);
            SecurityContextHolder.getContext().setAuthentication(userPrincipalAuthenticationToken);
        } 
        filterChain.doFilter(request, response);   
    }
}
