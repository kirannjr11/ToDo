package com.example.ToDo.controller;

import com.example.ToDo.dto.UserDTO;
import com.example.ToDo.model.JwtRequest;
import com.example.ToDo.model.JwtResponse;
import com.example.ToDo.security.JwtHelper;
import com.example.ToDo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserService userService;


    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> registerUser(@RequestBody UserDTO userDTO) {
        // Register the user (handled by UserService)
        userService.createUser(userDTO);

        // Authenticate the user after registration
        authenticate(userDTO.getName(), userDTO.getPassword());

        // Load the user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getName());

        // Generate JWT token
        String token = jwtHelper.generateToken(userDetails);

        // Return the JWT token in the response
        JwtResponse jwtResponse = new JwtResponse(token, userDetails.getUsername());
        return new ResponseEntity<>(jwtResponse, HttpStatus.CREATED);
    }

    // Helper method for authenticating the user
    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    // Exception handler for BadCredentialsException
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
}
