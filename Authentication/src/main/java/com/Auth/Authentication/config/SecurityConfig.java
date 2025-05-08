package com.Auth.Authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig<SecurityFilterChain> {

    @Bean
    public SecurityFilterChain filterChain(HttpEntity http) throws Exception {
        http
            .authorizeHttpRequests()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            .and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                .permitAll();
        return http.build();
    }
}
