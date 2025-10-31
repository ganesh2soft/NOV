package com.hcl.springecomapp.service;

import com.hcl.springecomapp.payload.*;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {

    // Login user and return JWT cookie + user info
    AuthenticationResult login(LoginRequest loginRequest);

    // Register new user
    ResponseEntity<?> register(SignupRequest signupRequest);

    // Logout user (clear JWT cookie)
    ResponseCookie logoutUser();

    // Get current logged-in user info
    UserInfoResponse getCurrentUserDetails(Authentication authentication);
}
