package com.example.demo.service.impl;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getUsername(), user.getEmail(), ""))
                .collect(Collectors.toList());
    }

    @Override
    public void registerUser(UserDto userDto) {
        logger.info("Registering new user: {}", userDto.getUsername());
        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(newUser);
        logger.info("User registered successfully with ID: {}", newUser.getId());
    }

    @Override
    public void editUser(UserDto userDto) {
        User existingUser = userRepository.findByUsername(userDto.getUsername());
        if (existingUser != null) {
            existingUser.setEmail(userDto.getEmail());
            // Update other fields as necessary
            userRepository.save(existingUser);
            logger.info("User updated successfully: {}", userDto.getUsername());
        } else {
            logger.error("User not found: {}", userDto.getUsername());
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public void updateUser(Long userId, UserDto userDto) {
        logger.info("Updating user with ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        userRepository.save(user);
        logger.info("User updated successfully with ID: {}", user.getId());
    }

    @Override
    public boolean loginUser(UserDto userDto) {
        logger.info("Attempting to login user: {}", userDto.getUsername());
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user != null) {
            if (passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                logger.info("User logged in successfully: {}", userDto.getUsername());
                return true;
            } else {
                logger.warn("Encoded password does not look like BCrypt or does not match for user: {}", userDto.getUsername());
            }
        } else {
            logger.warn("User not found in database: {}", userDto.getUsername());
        }
        logger.info("Login failed for user: {}", userDto.getUsername());
        return false;
    }

    @Override
    public UserDto getUserById(Long userId) {
        logger.info("Fetching user by ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), "");
    }

    @Override
    public void deleteUser(Long userId) {
        logger.info("Deleting user with ID: {}", userId);
        userRepository.deleteById(userId);
        logger.info("User deleted successfully");
    }

    @Override
    public boolean validateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            logger.info("User credentials are valid for user: {}", username);
            return true;
        } else {
            logger.info("Invalid credentials for user: {}", username);
            return false;
        }
    }
}
