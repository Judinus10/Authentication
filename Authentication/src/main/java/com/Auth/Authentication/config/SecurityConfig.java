package com.Auth.Authentication.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()  // Updated for Spring Security 6.x
                .requestMatchers("/css/**", "/login", "/register").permitAll() // Make sure login/register pages are accessible
                .anyRequest().authenticated()  // All other URLs require authentication
            .and()
                .formLogin()
                    .loginPage("/login")  // Specify the login page
                    .defaultSuccessUrl("/", true)  // After login, redirect to home
                    .permitAll()
            .and()
                .logout()
                    .logoutSuccessUrl("/login?logout")  // On logout, redirect to login page
                    .permitAll();
        return http.build();  // This works in Spring Boot 3.x+
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
