package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakura_arqui.sakura_backend.dto.PatientDto;
import sakura_arqui.sakura_backend.exception.ResourceNotFoundException;
import sakura_arqui.sakura_backend.model.DocumentType;
import sakura_arqui.sakura_backend.model.District;
import sakura_arqui.sakura_backend.model.Gender;
import sakura_arqui.sakura_backend.model.Patient;
import sakura_arqui.sakura_backend.repository.DocumentTypeRepository;
import sakura_arqui.sakura_backend.repository.DistrictRepository;
import sakura_arqui.sakura_backend.repository.GenderRepository;
import sakura_arqui.sakura_backend.repository.PatientRepository;
import sakura_arqui.sakura_backend.service.PatientService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {
    
    private final PatientRepository patientRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final GenderRepository genderRepository;
    private final DistrictRepository districtRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> findById(Integer id) {
        return patientRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> findByDni(String dni) {
        return patientRepository.findByDni(dni);
    }
    
    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }
    
    @Override
    public Patient update(Integer id, PatientDto patientDto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));
        
        updatePatientFromDto(patient, patientDto);
        return patientRepository.save(patient);
    }
    
    @Override
    public void deleteById(Integer id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Patient> findBySearchTerm(String searchTerm) {
        return patientRepository.findBySearchTerm(searchTerm);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Patient> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return patientRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByDni(String dni) {
        return patientRepository.existsByDni(dni);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return patientRepository.existsByEmail(email);
    }
    
    @Override
    public Patient createPatient(PatientDto patientDto) {
        if (patientDto.getDni() != null && patientRepository.existsByDni(patientDto.getDni())) {
            throw new IllegalArgumentException("DNI already exists");
        }
        
        if (patientDto.getEmail() != null && patientRepository.existsByEmail(patientDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        Patient patient = new Patient();
        updatePatientFromDto(patient, patientDto);
        return patientRepository.save(patient);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Patient> findRecentPatients(int limit) {
        return patientRepository.findAll().stream()
                .sorted((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .limit(limit)
                .toList();
    }
    
    private void updatePatientFromDto(Patient patient, PatientDto patientDto) {
        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setDni(patientDto.getDni());
        patient.setBirthDate(patientDto.getBirthDate());
        patient.setPhoneNumber(patientDto.getPhone());
        patient.setEmail(patientDto.getEmail());
        patient.setStatus(patientDto.getStatus() != null ? patientDto.getStatus() : true);
        
        // Set document type
        if (patientDto.getDocumentTypeId() != null) {
            DocumentType documentType = documentTypeRepository.findById(patientDto.getDocumentTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Document type not found with id: " + patientDto.getDocumentTypeId()));
            patient.setDocumentType(documentType);
        }
        
        // Set gender
        if (patientDto.getGenderId() != null) {
            Gender gender = genderRepository.findById(patientDto.getGenderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Gender not found with id: " + patientDto.getGenderId()));
            patient.setGender(gender);
        }
        
        // Set district
        if (patientDto.getDistrictId() != null) {
            District district = districtRepository.findById(patientDto.getDistrictId())
                    .orElseThrow(() -> new ResourceNotFoundException("District not found with id: " + patientDto.getDistrictId()));
            patient.setDistrict(district);
        }
    }
} 