package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.UserDto;
import sakura_arqui.sakura_backend.model.User;
import sakura_arqui.sakura_backend.service.UserService;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDto userDto) {
        User createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @Valid @RequestBody UserDto userDto) {
        User updatedUser = userService.update(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/role/{rolId}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Integer rolId) {
        List<User> users = userService.findByRole(rolId);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        List<User> users = userService.findActiveUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/exists/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/public/info")
    public ResponseEntity<?> getPublicInfo() {
        // Public endpoint for welcome page
        return ResponseEntity.ok(Map.of(
            "appName", "Sakura Dental Clinic",
            "version", "1.0.0",
            "status", "running"
        ));
    }
    
    @PutMapping("/{id}/password")
    public ResponseEntity<Boolean> changePassword(@PathVariable Integer id, 
                                                 @RequestParam String oldPassword, 
                                                 @RequestParam String newPassword) {
        boolean changed = userService.changePassword(id, oldPassword, newPassword);
        return ResponseEntity.ok(changed);
    }

    @GetMapping("/username")
    public ResponseEntity<?> getCurrentUsername(Authentication authentication) {
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof sakura_arqui.sakura_backend.model.User user) {
                return ResponseEntity.ok(Map.of("username", user.getUsername()));
            } else if (authentication.getPrincipal() instanceof String username) {
                return ResponseEntity.ok(Map.of("username", username));
            }
        }
        // Since security is disabled for testing, return a default response
        return ResponseEntity.ok(Map.of(
            "username", null,
            "authenticated", false,
            "message", "No user currently authenticated"
        ));
    }
    
    @GetMapping("/check-auth")
    public ResponseEntity<?> checkAuthenticationStatus() {
        // This endpoint is specifically for the welcome page to check auth status
        return ResponseEntity.ok(Map.of(
            "authenticated", false,
            "message", "Authentication check endpoint - security is currently disabled for testing"
        ));
    }
} 