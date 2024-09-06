package com.example.ToDo.controller;

import com.example.ToDo.dto.UserDTO;
import com.example.ToDo.exception.InvalidUserDataException;
import com.example.ToDo.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {

        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            throw new InvalidUserDataException("User not found with ID: " + id);
        }
        return userDTO;
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO existingUserDTO = userService.getUserById(id);
        if (existingUserDTO == null) {
            throw new InvalidUserDataException("User not found with ID: " + id);
        }
        return userService.updateUser(id, userDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        if (userDTO == null) {
            throw new InvalidUserDataException("User not found with ID: " + id);
        }
        userService.deleteUser(id);
    }


}
