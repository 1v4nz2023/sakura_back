package sakura_arqui.sakura_backend.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.UserDto;
import sakura_arqui.sakura_backend.model.User;
import sakura_arqui.sakura_backend.security.JwtUtil;
import sakura_arqui.sakura_backend.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            // Log the incoming request for debugging
            System.out.println("Login attempt - Username: " + (loginRequest != null ? loginRequest.getUsername() : "null"));
            
            if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
                System.out.println("Login failed - Missing username or password");
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Username and password are required",
                    "error", "missing_credentials"
                ));
            }
            
            String input = loginRequest.getUsername();
            Optional<User> userOpt;
            if (input.contains("@")) {
                userOpt = userService.findByEmail(input);
                System.out.println("Searching by email: " + input);
            } else {
                userOpt = userService.findByUsername(input);
                System.out.println("Searching by username: " + input);
            }
            
            if (userOpt.isEmpty()) {
                System.out.println("User not found: " + input);
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Usuario no encontrado",
                    "error", "user_not_found"
                ));
            }
            
            User user = userOpt.get();
            System.out.println("User found: " + user.getUsername() + ", Active: " + user.getIsActive());
            
            if (!user.getIsActive()) {
                System.out.println("User is inactive: " + user.getUsername());
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Usuario inactivo",
                    "error", "user_inactive"
                ));
            }
            
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
                System.out.println("Password mismatch for user: " + user.getUsername());
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Contraseña incorrecta",
                    "error", "invalid_password"
                ));
            }
            
            // Login successful
            System.out.println("Login successful for user: " + user.getUsername());
            userService.updateLastLogin(user.getUserId());
            String token = jwtUtil.generateToken(user.getUsername(), user.getRol().getName(), user.getUserId());
            
            System.out.println("Token generated successfully");
            
            ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false) // set to true in production (HTTPS)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .build();
            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            
            System.out.println("Cookie set successfully");
            
            // Create a simple response first to test
            Map<String, Object> resp = new HashMap<>();
            resp.put("success", true);
            resp.put("message", "Login successful");
            resp.put("token", token);
            resp.put("username", user.getUsername());
            resp.put("userId", user.getUserId());
            
            System.out.println("Response created, returning...");
            return ResponseEntity.ok(resp);
            
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Internal server error: " + e.getMessage(),
                "error", "internal_error"
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(Map.of("success", true, "message", "Logged out"));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        try {
            User createdUser = userService.createUser(userDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User registered successfully");
            
            // Create a safe user object without lazy relationships
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", createdUser.getUserId());
            userInfo.put("username", createdUser.getUsername());
            userInfo.put("email", createdUser.getEmail());
            userInfo.put("isActive", createdUser.getIsActive());
            userInfo.put("createdAt", createdUser.getCreatedAt());
            
            // Safely get role information
            if (createdUser.getRol() != null) {
                Map<String, Object> roleInfo = new HashMap<>();
                roleInfo.put("roleId", createdUser.getRol().getRoleId());
                roleInfo.put("name", createdUser.getRol().getName());
                roleInfo.put("descripcion", createdUser.getRol().getDescripcion());
                userInfo.put("rol", roleInfo);
            }
            
            response.put("user", userInfo);
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
                .map(user -> {
                    // Create a safe user object without lazy relationships
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("userId", user.getUserId());
                    userInfo.put("username", user.getUsername());
                    userInfo.put("email", user.getEmail());
                    userInfo.put("isActive", user.getIsActive());
                    userInfo.put("lastLogin", user.getLastLogin());
                    userInfo.put("createdAt", user.getCreatedAt());
                    
                    // Safely get role information
                    if (user.getRol() != null) {
                        Map<String, Object> roleInfo = new HashMap<>();
                        roleInfo.put("roleId", user.getRol().getRoleId());
                        roleInfo.put("name", user.getRol().getName());
                        roleInfo.put("descripcion", user.getRol().getDescripcion());
                        userInfo.put("rol", roleInfo);
                    }
                    
                    return ResponseEntity.ok(Map.of(
                        "authenticated", true,
                        "user", userInfo
                    ));
                })
                .orElse(ResponseEntity.ok(Map.of(
                    "authenticated", false
                )));
    }
    
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Auth controller is working!",
            "timestamp", System.currentTimeMillis()
        ));
    }
    
    @PostMapping("/test-login")
    public ResponseEntity<?> testLogin(@RequestBody LoginRequest loginRequest) {
        // Simple test endpoint that just validates the request format
        if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Invalid request format",
                "expected", Map.of(
                    "username", "string",
                    "password", "string"
                )
            ));
        }
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Request format is valid",
            "received", Map.of(
                "username", loginRequest.getUsername(),
                "passwordLength", loginRequest.getPassword().length()
            )
        ));
    }
    
    @GetMapping("/status")
    public ResponseEntity<?> getAuthStatus() {
        return ResponseEntity.ok(Map.of(
            "authenticated", false,
            "message", "Authentication status check - security is disabled for testing",
            "availableEndpoints", Map.of(
                "login", "POST /api/auth/login",
                "register", "POST /api/auth/register",
                "logout", "POST /api/auth/logout"
            )
        ));
    }
    
    public static class LoginRequest {
        @JsonProperty("username")
        private String username;
        
        @JsonProperty("password")
        private String password;
        
        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
} 