package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.PatientDto;
import sakura_arqui.sakura_backend.dto.PatientResponseDto;
import sakura_arqui.sakura_backend.service.PatientService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    
    private final PatientService patientService;
    
    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        List<PatientResponseDto> patients = patientService.findAllAsDto();
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable Integer id) {
        return patientService.findByIdAsDto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/dni/{dni}")
    public ResponseEntity<PatientResponseDto> getPatientByDni(@PathVariable String dni) {
        return patientService.findByDniAsDto(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientDto patientDto) {
        PatientResponseDto createdPatient = patientService.createPatientAndReturnDto(patientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientDto patientDto) {
        PatientResponseDto updatedPatient = patientService.updateAndReturnDto(id, patientDto);
        return ResponseEntity.ok(updatedPatient);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<PatientResponseDto>> searchPatients(@RequestParam String searchTerm) {
        List<PatientResponseDto> patients = patientService.findBySearchTermAsDto(searchTerm);
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<PatientResponseDto>> getPatientsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<PatientResponseDto> patients = patientService.findByDateRange(startDate, endDate).stream()
                .map(patient -> patientService.findByIdAsDto(patient.getPatientId()).orElse(null))
                .filter(dto -> dto != null)
                .toList();
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<PatientResponseDto>> getRecentPatients(@RequestParam(defaultValue = "10") int limit) {
        List<PatientResponseDto> patients = patientService.findRecentPatientsAsDto(limit);
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/exists/dni/{dni}")
    public ResponseEntity<Boolean> checkDniExists(@PathVariable String dni) {
        boolean exists = patientService.existsByDni(dni);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = patientService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
} 