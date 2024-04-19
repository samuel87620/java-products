package com.example.demo.service;

import com.example.demo.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();
    void registerUser(UserDto userDto);

    void editUser(UserDto userDto);
    void updateUser(Long userId, UserDto userDto);
    boolean loginUser(UserDto userDto);
    UserDto getUserById(Long userId);
    void deleteUser(Long userId);
    boolean validateUser(String username, String password);
}
