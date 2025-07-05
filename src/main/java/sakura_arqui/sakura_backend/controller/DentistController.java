package sakura_arqui.sakura_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.model.Dentist;
import sakura_arqui.sakura_backend.repository.DentistRepository;

import java.util.List;

@RestController
@RequestMapping("/api/dentists")
@RequiredArgsConstructor
public class DentistController {
    
    private final DentistRepository dentistRepository;
    
    @GetMapping
    public ResponseEntity<List<Dentist>> getAllDentists() {
        List<Dentist> dentists = dentistRepository.findAll();
        return ResponseEntity.ok(dentists);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Dentist> getDentistById(@PathVariable Integer id) {
        return dentistRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Dentist> createDentist(@RequestBody Dentist dentist) {
        Dentist createdDentist = dentistRepository.save(dentist);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDentist);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Dentist> updateDentist(@PathVariable Integer id, @RequestBody Dentist dentist) {
        if (!dentistRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        dentist.setDentistId(id);
        Dentist updatedDentist = dentistRepository.save(dentist);
        return ResponseEntity.ok(updatedDentist);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDentist(@PathVariable Integer id) {
        if (!dentistRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        dentistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Dentist>> getActiveDentists() {
        List<Dentist> dentists = dentistRepository.findByActiveTrue();
        return ResponseEntity.ok(dentists);
    }
    
    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<Dentist>> getDentistsBySpecialty(@PathVariable String specialty) {
        List<Dentist> dentists = dentistRepository.findBySpecialtyContainingIgnoreCase(specialty);
        return ResponseEntity.ok(dentists);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Dentist>> searchDentists(@RequestParam String searchTerm) {
        List<Dentist> dentists = dentistRepository.findBySearchTerm(searchTerm);
        return ResponseEntity.ok(dentists);
    }
    
    @GetMapping("/specialties")
    public ResponseEntity<List<String>> getAllSpecialties() {
        List<String> specialties = dentistRepository.findAllSpecialties();
        return ResponseEntity.ok(specialties);
    }
} 