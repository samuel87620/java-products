package com.example.demo.service.impl;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserDto userDto) {
        // Encrypt the user's password
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        // Log the encoded password for debugging purposes
        // 注意：實際部署時應該移除以下打印語句，因為它們可能會記錄敏感信息
        System.out.println("Encoded Password: " + encodedPassword);

        // Create a new user and set the encrypted password
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(encodedPassword);

        // Save the new user to the repository (database)
        userRepository.save(user);
    }

    @Override
    public boolean loginUser(UserDto userDto) {
        // Find the user by username
        User user = userRepository.findByUsername(userDto.getUsername());
        // Check if user exists and if the password matches
        return user != null && passwordEncoder.matches(userDto.getPassword(), user.getPassword());
    }

    @Override
    public UserDto getUserById(Long userId) {
        // Find the user by ID
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        // Convert to UserDto and return (without password for security)
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        return userDto;
    }
}
