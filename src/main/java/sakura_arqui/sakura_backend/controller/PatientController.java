package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.PatientDto;
import sakura_arqui.sakura_backend.model.Patient;
import sakura_arqui.sakura_backend.service.PatientService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    
    private final PatientService patientService;
    
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = patientService.findAll();
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Integer id) {
        return patientService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/dni/{dni}")
    public ResponseEntity<Patient> getPatientByDni(@PathVariable String dni) {
        return patientService.findByDni(dni)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody PatientDto patientDto) {
        Patient createdPatient = patientService.createPatient(patientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Integer id, @Valid @RequestBody PatientDto patientDto) {
        Patient updatedPatient = patientService.update(id, patientDto);
        return ResponseEntity.ok(updatedPatient);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Patient>> searchPatients(@RequestParam String searchTerm) {
        List<Patient> patients = patientService.findBySearchTerm(searchTerm);
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<Patient>> getPatientsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Patient> patients = patientService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(patients);
    }
    
    @GetMapping("/recent")
    public ResponseEntity<List<Patient>> getRecentPatients(@RequestParam(defaultValue = "10") int limit) {
        List<Patient> patients = patientService.findRecentPatients(limit);
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