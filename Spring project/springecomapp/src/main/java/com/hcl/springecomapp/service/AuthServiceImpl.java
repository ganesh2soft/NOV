package com.hcl.springecomapp.service;

import com.hcl.springecomapp.entity.AppRole;

import com.hcl.springecomapp.entity.Users;
import com.hcl.springecomapp.jwt.JwtUtils;
import com.hcl.springecomapp.payload.*;
import com.hcl.springecomapp.repository.UsersRepository;
import com.hcl.springecomapp.security.UserDetailsImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public AuthenticationResult login(LoginRequest loginRequest) {
    	
    	System.out.println("Login Request :" + loginRequest.getUserName()+ " --- " + loginRequest.getPassword());
    	
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Generate JWT cookie
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails.getUsername());

        // Build user info response
        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse userInfo = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles
                 // token is stored in cookie
        );

        return new AuthenticationResult(jwtCookie, userInfo);
    }

    @Override
    public ResponseEntity<?> register(SignupRequest signUpRequest) {

        if (usersRepository.existsByUserName(signUpRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (usersRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user
        Users user = new Users(
                signUpRequest.getUserName(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        // Set roles
        Set<String> strRoles = signUpRequest.getRole();
        Set<AppRole> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            roles.add(AppRole.USER); // default
        } else {
            strRoles.forEach(role -> {
                if (role.equalsIgnoreCase("ADMIN")) {
                    roles.add(AppRole.ADMIN);
                } else {
                    roles.add(AppRole.USER);
                }
            });
        }

        user.setRoles(roles);
        usersRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @Override
    public ResponseCookie logoutUser() {
        return jwtUtils.getCleanJwtCookie(); // clear cookie
    }

    @Override
    public UserInfoResponse getCurrentUserDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        return new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles, null);
    }
}
