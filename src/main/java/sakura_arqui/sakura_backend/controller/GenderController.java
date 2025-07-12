package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.GenderDto;
import sakura_arqui.sakura_backend.model.Gender;
import sakura_arqui.sakura_backend.service.GenderService;

import java.util.List;

@RestController
@RequestMapping("/api/genders")
@RequiredArgsConstructor
public class GenderController {
    
    private final GenderService genderService;
    
    @GetMapping
    public ResponseEntity<List<Gender>> getAllGenders() {
        List<Gender> genders = genderService.findAll();
        return ResponseEntity.ok(genders);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Gender>> getActiveGenders() {
        List<Gender> genders = genderService.findActive();
        return ResponseEntity.ok(genders);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Gender> getGenderById(@PathVariable Integer id) {
        return genderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Gender> getGenderByCode(@PathVariable String code) {
        return genderService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<Gender> getGenderByName(@PathVariable String name) {
        return genderService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Gender> createGender(@Valid @RequestBody GenderDto genderDto) {
        Gender createdGender = genderService.createGender(genderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGender);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Gender> updateGender(@PathVariable Integer id, @Valid @RequestBody GenderDto genderDto) {
        Gender updatedGender = genderService.update(id, genderDto);
        return ResponseEntity.ok(updatedGender);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGender(@PathVariable Integer id) {
        genderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Gender>> searchGenders(@RequestParam String searchTerm) {
        List<Gender> genders = genderService.findBySearchTerm(searchTerm);
        return ResponseEntity.ok(genders);
    }
} 