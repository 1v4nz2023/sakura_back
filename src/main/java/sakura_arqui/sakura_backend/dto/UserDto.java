package sakura_arqui.sakura_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sakura_arqui.sakura_backend.model.User;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private Integer userId;
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String passwordHash;
    
    @NotNull(message = "Role is required")
    private User.UserRole role;
    
    private Boolean isActive = true;
    
    private LocalDateTime lastLogin;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
} 