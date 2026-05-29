package com.fourbitlabs.employee_management_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/")
    public String helloMsg() {
        return "Welcome to EMS backend!";
    }
}
