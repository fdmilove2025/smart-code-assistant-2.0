package com.fdmgroup.smart_code_assistant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fdmgroup.smart_code_assistant.model.User;
import com.fdmgroup.smart_code_assistant.service.UserService;

@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok(currentUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving user profile");
        }
    }
    
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserProfileUpdateRequest request) {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
            // Update username if provided
            if (request.getUsername() != null && !request.getUsername().isEmpty()) {
                // Check if username is already taken by another user
                if (!currentUser.getUsername().equals(request.getUsername()) && 
                    userService.getUserByUsername(request.getUsername()).isPresent()) {
                    return ResponseEntity.badRequest().body("Username already exists");
                }
                currentUser.setUsername(request.getUsername());
            }
            
            // Update email if provided
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                // Check if email is already taken by another user
                if (!currentUser.getEmail().equals(request.getEmail()) && 
                    userService.getUserByEmail(request.getEmail()).isPresent()) {
                    return ResponseEntity.badRequest().body("Email already exists");
                }
                currentUser.setEmail(request.getEmail());
            }
            
            // Update password if provided
            if (request.getCurrentPassword() != null && !request.getCurrentPassword().isEmpty() &&
                request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
                
                // Verify current password
                if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
                    return ResponseEntity.badRequest().body("Current password is incorrect");
                }
                
                // Update to new password
                currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            }
            
            // Save the updated user
            User updatedUser = userService.updateUser(currentUser);
            
            // Return updated user info (excluding sensitive data)
            UserProfileResponse response = new UserProfileResponse(
                updatedUser.getUsername(),
                updatedUser.getEmail()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating user profile: " + e.getMessage());
        }
    }
}

class UserProfileUpdateRequest {
    private String username;
    private String email;
    private String currentPassword;
    private String newPassword;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCurrentPassword() {
        return currentPassword;
    }
    
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

class UserProfileResponse {
    private String username;
    private String email;
    
    public UserProfileResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
} 