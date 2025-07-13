package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.GenderDto;
import sakura_arqui.sakura_backend.service.GenderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/genders")
@RequiredArgsConstructor
public class GenderController {
    
    private final GenderService genderService;
    
    @GetMapping
    public ResponseEntity<List<GenderDto>> getAllGenders() {
        List<GenderDto> genders = genderService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(genders);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<GenderDto>> getActiveGenders() {
        List<GenderDto> genders = genderService.findActive().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(genders);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GenderDto> getGenderById(@PathVariable Integer id) {
        return genderService.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<GenderDto> getGenderByCode(@PathVariable String code) {
        return genderService.findByCode(code)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<GenderDto> getGenderByName(@PathVariable String name) {
        return genderService.findByName(name)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<GenderDto> createGender(@Valid @RequestBody GenderDto genderDto) {
        var createdGender = genderService.createGender(genderDto);
        GenderDto responseDto = convertToDto(createdGender);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GenderDto> updateGender(@PathVariable Integer id, @Valid @RequestBody GenderDto genderDto) {
        var updatedGender = genderService.update(id, genderDto);
        GenderDto responseDto = convertToDto(updatedGender);
        return ResponseEntity.ok(responseDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGender(@PathVariable Integer id) {
        genderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<GenderDto>> searchGenders(@RequestParam String searchTerm) {
        List<GenderDto> genders = genderService.findBySearchTerm(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(genders);
    }
    
    private GenderDto convertToDto(sakura_arqui.sakura_backend.model.Gender gender) {
        return new GenderDto(
            gender.getGenderId(),
            gender.getCode(),
            gender.getName(),
            gender.isStatus()
        );
    }
} 