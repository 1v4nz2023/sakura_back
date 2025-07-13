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
public class PatientDto {
    private Integer patientId;

    @NotBlank(message = "First name is required")
    @Size(max = 80, message = "First name must be less than 80 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 80, message = "Last name must be less than 80 characters")
    private String lastName;

    @NotBlank(message = "DNI is required")
    @Pattern(regexp = "^[0-9]{8}$", message = "DNI must be exactly 8 digits")
    private String dni;

    @NotNull(message = "Birth date is required")
    private LocalDateTime birthDate;

    // Para entrada (POST/PUT)
    private Integer documentTypeId;
    private Integer genderId;
    private Integer districtId;

    // Para respuesta (GET)
    private DocumentTypeDto documentType;
    private GenderDto gender;
    private DistrictDto district;

    @Pattern(regexp = "^[+]?[0-9\\s-()]{7,20}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @Size(max = 120, message = "Email must be less than 120 characters")
    private String email;

    private Boolean status = true;
    private LocalDateTime createdAt;
} 