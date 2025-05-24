package com.Auth.Authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/register", "/forgot-password", "/reset_password", "/otp_confirmation",
                                "/register_successfull", "/reset_success", "/h2-console/**", "/css/**", "/js/**")
                        .permitAll()
                        // Allow unauthenticated access to these URLs
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page path
                        .permitAll() // Allow access to the login page
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // Custom login page for OAuth2
                        .defaultSuccessUrl("/home", true) // Redirect here after successful login
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Redirect after logout
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // Disable CSRF for H2 Console
                )
                .headers(headers -> headers
                        .frameOptions().disable() // Allow H2 Console to be rendered in a frame
                );

        return http.build(); // Build and return the security filter chain
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Bean for encoding passwords using BCrypt
    }
}
