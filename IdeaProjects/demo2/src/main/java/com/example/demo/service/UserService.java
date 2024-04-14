package com.example.demo.service;

import com.example.demo.dto.UserDto;

public interface UserService {
    void registerUser(UserDto userDto);
    boolean loginUser(UserDto userDto);
    UserDto getUserById(Long userId);
}
