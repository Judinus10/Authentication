package com.Auth.Authentication.controller;

import com.Auth.Authentication.model.User;
import com.Auth.Authentication.repository.UserRepository;
// import com.Auth.Authentication.service.EmailService;
import com.Auth.Authentication.service.OtpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OtpService otpService;

    // @Autowired
    // private EmailService emailService;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

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
        if (existing != null && passwordEncoder.matches(user.getPassword(), existing.getPassword())) {
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

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        String otp = otpService.generateOtp();
        otpService.saveOtp(user.getEmail(), otp);
        // emailService.sendOtpEmail(user.getEmail(), otp);

        redirectAttributes.addFlashAttribute("email", user.getEmail());
        redirectAttributes.addFlashAttribute("flow", "register");
        return "redirect:/otp_confirmation";
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
            String otp = otpService.generateOtp();
            otpService.saveOtp(email, otp);
            // emailService.sendOtpEmail(email, otp);

            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("flow", "forgot");
            return "redirect:/otp_confirmation";
        } else {
            redirectAttributes.addFlashAttribute("error", "No user found with this email.");
            return "redirect:/otp_confirmation";
        }
    }

    // Show OTP confirmation page
    @GetMapping("/otp_confirmation")
    public String showOtpPage(@ModelAttribute("email") String email,
            @ModelAttribute("error") String error,
            @ModelAttribute("flow") String flow,
            Model model) {
        model.addAttribute("email", email);
        model.addAttribute("error", error);
        model.addAttribute("flow", flow);
        return "otp_confirmation";
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String otp,
            @RequestParam("email") String email,
            @RequestParam("flow") String flow,
            RedirectAttributes redirectAttributes) {

        String expectedOtp = otpService.getOtp(email);

        if (expectedOtp != null && otp.equals(expectedOtp)) {
            otpService.clearOtp(email);

            if ("register".equalsIgnoreCase(flow)) {
                return "redirect:/login";
            } else if ("forgot".equalsIgnoreCase(flow)) {
                return "redirect:/reset_password?email=" + email;
            } else {
                redirectAttributes.addFlashAttribute("error", "Unknown flow type.");
                return "redirect:/otp_confirmation";
            }

        } else {
            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("flow", flow);
            redirectAttributes.addFlashAttribute("error", "Invalid OTP. Please try again.");
            return "redirect:/otp_confirmation";
        }
    }

    // Resend OTP
    @PostMapping("/resend-otp")
    public String resendOtp(@RequestParam("email") String email,
            @RequestParam("flow") String flow,
            RedirectAttributes redirectAttributes) {

        String otp = otpService.generateOtp();
        otpService.saveOtp(email, otp);
        // emailService.sendOtpEmail(email, otp);

        redirectAttributes.addFlashAttribute("email", email);
        redirectAttributes.addFlashAttribute("flow", flow);
        return "redirect:/otp_confirmation";
    }

    // Show reset password form
    @GetMapping("/reset_password")
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
    @PostMapping("/reset_password")
    public String resetPassword(@RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/reset_password?email=" + email;
        }

        User user = userRepo.findByEmail(email);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/reset_password?email=" + email;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        redirectAttributes.addFlashAttribute("success", "Password reset successfully!");
        return "redirect:/reset_password?email=" + email;
    }
}
