package sakura_arqui.sakura_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    
    private Integer employeeId;
    
    @NotNull(message = "Document number is required")
    private Integer docNumber;
    
    @NotBlank(message = "First name is required")
    @Size(max = 80, message = "First name must be less than 80 characters")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Size(max = 80, message = "Last name must be less than 80 characters")
    private String lastName;
    
    @Size(max = 100, message = "Specialty must be less than 100 characters")
    private String specialty;
    
    @NotNull(message = "Hire date is required")
    private LocalDateTime hiredAt;
    
    @Pattern(regexp = "^[+]?[0-9\\s-()]{7,20}$", message = "Invalid phone number format")
    private String phone;
    
    @Email(message = "Invalid email format")
    @Size(max = 120, message = "Email must be less than 120 characters")
    private String email;
    
    private Boolean status = true;
    
    @NotNull(message = "Document type ID is required")
    private Integer documentTypeId;
    
    @NotNull(message = "Job title ID is required")
    private Integer jobTitleId;
    
    @NotNull(message = "Gender ID is required")
    private Integer genderId;
    
    @NotNull(message = "District ID is required")
    private Integer districtId;
    
    // User account information (optional for employee creation)
    private String username;
    private String password;
    private Integer roleId;
} 