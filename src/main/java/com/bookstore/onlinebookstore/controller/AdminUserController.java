package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.User;
import com.bookstore.onlinebookstore.service.AdminUserService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return adminUserService.getAllUsers();
    }

    @PutMapping("/{userId}/enable")
    public void enableUser(@PathVariable int userId) {
        adminUserService.toggleUser(userId, true);
    }

    @PutMapping("/{userId}/disable")
    public void disableUser(@PathVariable int userId) {
        adminUserService.toggleUser(userId, false);
    }
}
