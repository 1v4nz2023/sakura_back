package sakura_arqui.sakura_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {
    
    private Integer patientId;
    
    @NotBlank(message = "First name is required")
    @Size(max = 80, message = "First name must be less than 80 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 80, message = "Last name must be less than 80 characters")
    private String lastName;
    
    @Pattern(regexp = "^[0-9]{8}$", message = "DNI must be exactly 8 digits")
    private String dni;
    
    @Pattern(regexp = "^[+]?[0-9\\s-()]{7,20}$", message = "Invalid phone number format")
    private String phone;
    
    @Email(message = "Invalid email format")
    @Size(max = 120, message = "Email must be less than 120 characters")
    private String email;
    
    private LocalDateTime createdAt;
} 