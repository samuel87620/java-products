package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserViewController {

    private final UserService userService;

    @Autowired
    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<UserDto>> listUsers() {
        List<UserDto> usersList = userService.findAllUsers();
        return ResponseEntity.ok(usersList);
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        UserDto user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            return "editUser"; // View name to match Thymeleaf template
        } else {
            model.addAttribute("errorMessage", "No user found with ID " + id);
            return "redirect:/user/users"; // Redirect back to the user list if user not found
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/update/{id}")
    @ResponseBody // 这个注解确保返回的是响应体
    public ResponseEntity<?> updateUserAjax(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            userService.updateUser(id, userDto);
            // 返回成功响应体
            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            // 返回错误响应体和适当的 HTTP 状态代码
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + e.getMessage());
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // View name for login, Thymeleaf will render /templates/login.html
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute UserDto userDto, Model model) {
        try {
            boolean isValidUser = userService.validateUser(userDto.getUsername(), userDto.getPassword());
            if (isValidUser) {
                model.addAttribute("user", userDto);
                return "redirect:/user/users";
            } else {
                model.addAttribute("loginError", "Invalid username or password");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("loginError", "Login error: " + e.getMessage());
            return "login";
        }
    }
}
