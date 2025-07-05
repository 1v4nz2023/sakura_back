package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.PatientDto;
import sakura_arqui.sakura_backend.model.Patient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    
    List<Patient> findAll();
    
    Optional<Patient> findById(Integer id);
    
    Optional<Patient> findByDni(String dni);
    
    Patient save(Patient patient);
    
    Patient update(Integer id, PatientDto patientDto);
    
    void deleteById(Integer id);
    
    List<Patient> findBySearchTerm(String searchTerm);
    
    List<Patient> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    boolean existsByDni(String dni);
    
    boolean existsByEmail(String email);
    
    Patient createPatient(PatientDto patientDto);
    
    List<Patient> findRecentPatients(int limit);
} 