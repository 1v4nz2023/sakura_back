package sakura_arqui.sakura_backend.controller;

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
        String input = loginRequest.getUsername();
        Optional<User> userOpt;
        if (input.contains("@")) {
            userOpt = userService.findByEmail(input);
        } else {
            userOpt = userService.findByUsername(input);
        }
        return userOpt
                .filter(user -> user.getIsActive())
                .filter(user -> passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash()))
                .map(user -> {
                    userService.updateLastLogin(user.getUserId());
                    String token = jwtUtil.generateToken(user.getUsername(), user.getRol().getName(), user.getUserId());
                    ResponseCookie cookie = ResponseCookie.from("token", token)
                        .httpOnly(true)
                        .secure(false) // set to true in production (HTTPS)
                        .path("/")
                        .maxAge(24 * 60 * 60)
                        .sameSite("Strict")
                        .build();
                    response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
                    Map<String, Object> resp = new HashMap<>();
                    resp.put("success", true);
                    resp.put("user", user);
                    resp.put("message", "Login successful");
                    return ResponseEntity.ok(resp);
                })
                .orElse(ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Contraseña o usuario incorrecto"
                )));
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