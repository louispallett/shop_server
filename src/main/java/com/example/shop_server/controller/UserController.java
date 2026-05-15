package com.example.shop_server.controller;

import com.example.shop_server.dto.AuthResponse;
import com.example.shop_server.dto.LoginRequest;
import com.example.shop_server.model.User;
import com.example.shop_server.repository.UserRepository;
import com.example.shop_server.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get")
    public ResponseEntity<User> getUser(@RequestParam String email) {
        return userRepository.findByEmail(email.toLowerCase())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User incomingUser) {

        // Check if email already used
        if (userRepository.findByEmail(incomingUser.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Email already registered");
        }

        incomingUser.setEmail(incomingUser.getEmail());

        // Hashing password
        String hashedPassword = passwordEncoder.encode((incomingUser.getPassword()));
        incomingUser.setPassword(hashedPassword);

        User savedUser = userRepository.save(incomingUser);
        return ResponseEntity.ok(savedUser);
    }

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        var userOptional = userRepository.findByEmail(loginRequest.getEmail().toLowerCase());

        if (
                userOptional.isPresent() &&
                passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())
        ) {
            String token = jwtUtil.generateToken(userOptional.get().getEmail());
            return ResponseEntity.ok(new AuthResponse(token));
        }

        // Always return 403 for any login failure
        return ResponseEntity.status(403).body("Invalid Credentials");
    }

    // Do not do the below - it's critical that we don't return separate responses for username/password failure
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//        return userRepository.findByEmail(loginRequest.getEmail())
//                .map(user -> {
//                    if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
//                        String token = jwtUtil.generateToken(user.getEmail());
//                        return ResponseEntity.ok(new AuthResponse(token));
//                    } else {
//                        return ResponseEntity
//                                .status(401)
//                                .body("Incorrect Password");
//                    }
//                })
//                .orElseGet(() -> ResponseEntity
//                        .status(404)
//                        .body("User not found"));
//    }
}