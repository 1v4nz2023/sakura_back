package sakura_arqui.sakura_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.UserDto;
import sakura_arqui.sakura_backend.model.User;
import sakura_arqui.sakura_backend.service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return userService.findByUsername(loginRequest.getUsername())
                .filter(user -> user.getIsActive())
                .filter(user -> passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash()))
                .map(user -> {
                    // Update last login
                    userService.updateLastLogin(user.getUserId());
                    
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("user", user);
                    response.put("message", "Login successful");
                    
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid username or password"
                )));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        try {
            User createdUser = userService.createUser(userDto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", createdUser);
            response.put("message", "User registered successfully");
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(@RequestParam String username) {
        return userService.findByUsername(username)
                .map(user -> ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "user", user
                )))
                .orElse(ResponseEntity.ok(Map.of(
                    "authenticated", false
                )));
    }
    
    public static class LoginRequest {
        private String username;
        private String password;
        
        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
} 