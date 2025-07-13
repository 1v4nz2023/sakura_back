package sakura_arqui.sakura_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {
    
    private Integer patientId;
    private String firstName;
    private String lastName;
    private String dni;
    private LocalDateTime birthDate;
    private DocumentTypeResponseDto documentType;
    private GenderResponseDto gender;
    private DistrictResponseDto district;
    private String phoneNumber;
    private String email;
    private boolean status;
    private LocalDateTime createdAt;
} 