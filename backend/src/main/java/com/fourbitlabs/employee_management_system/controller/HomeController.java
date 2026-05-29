package com.fourbitlabs.employee_management_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to 4bitLabs Employee Management System (EMS) Backend!";
    }
}
