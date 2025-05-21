package com.fdmgroup.smart_code_assistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.smart_code_assistant.model.User;
import com.fdmgroup.smart_code_assistant.security.JwtService;
import com.fdmgroup.smart_code_assistant.service.UserService;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(
                    request.getUsername(),
                    request.getPassword(),
                    request.getEmail());
            String jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(jwtToken, "Registration successful", user.getEmail()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            User user = (User) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(jwtToken, "Login successful", user.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Invalid username or password", null));
        }
    }
}

class RegisterRequest {
    private String username;
    private String password;
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class AuthResponse {
    private String token;
    private String message;
    private String email;

    public AuthResponse(String token, String message, String email) {
        this.token = token;
        this.message = message;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}