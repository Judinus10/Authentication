package com.Auth.Authentication.controller;

import com.Auth.Authentication.model.User;
import com.Auth.Authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    // Show login page
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // Process login
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

    // Show register page
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Process register and redirect to OTP
    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model) {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        userRepo.save(user);

        // Send OTP to user's email (for now, assume it's sent)
        redirectAttributes.addFlashAttribute("email", user.getEmail());
        return "otp-confirmation";
    }

    // Root redirect
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // Show forgot password form
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot_password";
    }

    // Process forgot password and redirect to OTP
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes, Model model) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            // Send OTP (assume it's sent)
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/otp-confirmation";
        } else {
            model.addAttribute("error", "No user found with this email.");
            return "otp-confirmation";
        }
    }

    // Show OTP confirmation page
    @GetMapping("/otp-confirmation")
    public String showOtpPage(@ModelAttribute("email") String email, Model model) {
        model.addAttribute("email", email);
        return "otp-confirmation";
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp, @RequestParam("email") String email, Model model) {
        String expectedOtp = "123456"; // Replace with real OTP logic

        if (otp.equals(expectedOtp)) {
            model.addAttribute("email", email);
            return "redirect:/reset-password"; // Or home/dashboard if registration
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            model.addAttribute("email", email);
            return "otp-confirmation";
        }
    }

    // Reset password form (optional)
    @GetMapping("/reset-password")
    public String showResetPasswordForm() {
        return "reset_password"; // You can add this page separately
    }
}
