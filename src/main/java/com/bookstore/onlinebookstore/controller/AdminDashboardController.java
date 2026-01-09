package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.service.AdminDashboardService;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    public AdminDashboardController(AdminDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public Map<String, Object> overview() {

        return Map.of(
                "totalUsers", dashboardService.totalUsers(),
                "totalOrders", dashboardService.totalOrders(),
                "totalSales", dashboardService.totalSales(),
                "lowStockBooks", dashboardService.lowStockBooks());
    }
}
