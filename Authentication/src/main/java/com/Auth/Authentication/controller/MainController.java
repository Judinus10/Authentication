package com.Auth.Authentication.controller;

import com.Auth.Authentication.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute User user, Model model) {
        // Dummy check
        if ("admin".equals(user.getUsername()) && "1234".equals(user.getPassword())) {
            model.addAttribute("username", user.getUsername());
            return "home";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }
}
