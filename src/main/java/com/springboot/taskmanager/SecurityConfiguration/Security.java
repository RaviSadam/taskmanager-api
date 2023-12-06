package com.springboot.taskmanager.SecurityConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.taskmanager.SecurityFilters.JwtAuthFilter;
import com.springboot.taskmanager.UserServiceImplement.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor; 
@Configuration
@RequiredArgsConstructor
public class Security {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthFilter jwtAuthFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf((csrf)->
                csrf.disable()
            )
            .authorizeHttpRequests((auth)->
                auth    
                    .requestMatchers("/app/**").permitAll()
                    .anyRequest().authenticated()
            )
            .logout((logout)->
                logout
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
            )
            .sessionManagement((session)->
                session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
     
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(); 
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder); 
        return authenticationProvider; 
    }
}
