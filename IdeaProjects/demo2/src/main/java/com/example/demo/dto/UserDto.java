package com.example.demo.dto;

public class UserDto {
    private Long id; // 假設 ID 是 Long 類型
    private String username;
    private String email; // 添加 email 屬性
    private String password;

    // No-argument constructor
    public UserDto() {
    }

    // All-argument constructor
    public UserDto(Long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
