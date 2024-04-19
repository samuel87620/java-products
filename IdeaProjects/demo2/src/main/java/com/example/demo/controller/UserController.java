package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto);
            logger.info("User registered successfully");
            return ResponseEntity.ok("User registered successfully.");
        } catch (Exception e) {
            logger.error("Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDto userDto) {
        try {
            if (userService.loginUser(userDto)) {
                logger.info("User logged in successfully");
                return ResponseEntity.ok("User logged in successfully.");
            } else {
                logger.warn("Login failed for user: " + userDto.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed for user.");
            }
        } catch (Exception e) {
            logger.error("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        try {
            UserDto userDto = userService.getUserById(userId);
            if (userDto != null) {
                return ResponseEntity.ok(userDto);
            } else {
                logger.warn("User not found with ID: " + userId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error retrieving user by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> listUsers() {
        try {
            List<UserDto> users = userService.findAllUsers();
            if (users != null && !users.isEmpty()) {
                logger.info("Fetched all users successfully");
                return ResponseEntity.ok(users);
            } else {
                logger.warn("No users found");
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch users: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        try {
            userService.updateUser(userId, userDto);
            logger.info("User updated successfully for ID: {}", userId);
            return ResponseEntity.ok("User updated successfully.");
        } catch (Exception e) {
            logger.error("Update failed for user ID {}: {}", userId, e.getMessage());
            return ResponseEntity.badRequest().body("Update failed for user.");
        }
    }

}

