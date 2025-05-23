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
        redirectAttributes.addFlashAttribute("email", user.getEmail());
        return "redirect:/otp-confirmation";
    }

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
    public String forgotPassword(@RequestParam("email") String email,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/otp-confirmation";
        } else {
            redirectAttributes.addFlashAttribute("error", "No user found with this email.");
            // return "redirect:/forgot-password";
            return "redirect:/otp-confirmation";
        }
    }

    // Show OTP confirmation page
    @GetMapping("/otp-confirmation")
    public String showOtpPage(@ModelAttribute("email") String email,
                              @ModelAttribute("error") String error,
                              Model model) {
        model.addAttribute("email", email);
        model.addAttribute("error", error);
        return "otp-confirmation";
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,
                            @RequestParam("email") String email,
                            RedirectAttributes redirectAttributes) {
        String expectedOtp = "123456"; // Replace with real OTP logic

        if (otp.equals(expectedOtp)) {
            return "redirect:/reset-password?email=" + email;
        } else {
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("error", "Invalid OTP. Please try again.");
            return "redirect:/otp-confirmation";
        }
    }

    // Show reset password form
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam("email") String email,
                                        @ModelAttribute("error") String error,
                                        @ModelAttribute("success") String success,
                                        Model model) {
        model.addAttribute("email", email);
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "reset_password";
    }

    // Process reset password
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/reset-password?email=" + email;
        }

        User user = userRepo.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/reset-password?email=" + email;
        }

        user.setPassword(newPassword);
        userRepo.save(user);

        redirectAttributes.addFlashAttribute("success", "Password reset successfully!");
        return "redirect:/reset-password?email=" + email;
    }
}
