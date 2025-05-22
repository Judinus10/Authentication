package com.Auth.Authentication.controller;

import com.Auth.Authentication.model.User;
import com.Auth.Authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute User user, Model model) {
        User existing = userRepo.findByUsername(user.getUsername());
        if (existing != null && existing.getPassword().equals(user.getPassword())) {
            model.addAttribute("username", user.getUsername());
            return "home";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
public String processRegister(@ModelAttribute User user, Model model) {
    if (userRepo.findByUsername(user.getUsername()) != null) {
        model.addAttribute("error", "Username already exists");
        return "register";
    }

    userRepo.save(user);
    model.addAttribute("username", user.getUsername());
    return "register_successfull";
}


    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @PostMapping("/forgot-password")
public String forgotPassword(@RequestParam("email") String email, Model model) {
    // You can check if the email exists in your database
    User user = userRepo.findByEmail(email);  // Assuming you've added an email field to your User entity
    if (user != null) {
        // Send password reset link (in real-world, this would send an email with a reset link)
        // For now, we'll simulate this by redirecting to a success page
        model.addAttribute("message", "Password reset link sent to your email!");
    } else {
        model.addAttribute("error", "No user found with this email.");
    }
    return "reset_success";  // This can be a success page showing message
}

}
