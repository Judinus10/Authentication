package com.Auth.Authentication.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private Map<String, String> otpStorage = new HashMap<>();

    public String generateOtp() {
        Random random = new Random();
        int otpInt = 100000 + random.nextInt(900000);
        return String.valueOf(otpInt);
    }

    public void saveOtp(String email, String otp) {
        otpStorage.put(email, otp);
    }

    public String getOtp(String email) {
        return otpStorage.get(email);
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }
}
