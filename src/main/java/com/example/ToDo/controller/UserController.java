package com.example.ToDo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {
    @GetMapping("/normal")


    public ResponseEntity<String> normalUser() {
        return ResponseEntity.ok("yes, i am a normal user");
    }
}
