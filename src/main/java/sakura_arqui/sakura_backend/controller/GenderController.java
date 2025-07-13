package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.GenderDto;
import sakura_arqui.sakura_backend.dto.GenderResponseDto;
import sakura_arqui.sakura_backend.service.GenderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/genders")
@RequiredArgsConstructor
public class GenderController {
    
    private final GenderService genderService;
    
    @GetMapping
    public ResponseEntity<List<GenderResponseDto>> getAllGenders() {
        List<GenderResponseDto> genders = genderService.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(genders);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<GenderResponseDto>> getActiveGenders() {
        List<GenderResponseDto> genders = genderService.findActive().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(genders);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GenderResponseDto> getGenderById(@PathVariable Integer id) {
        return genderService.findById(id)
                .map(this::convertToResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<GenderResponseDto> getGenderByCode(@PathVariable String code) {
        return genderService.findByCode(code)
                .map(this::convertToResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<GenderResponseDto> getGenderByName(@PathVariable String name) {
        return genderService.findByName(name)
                .map(this::convertToResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<GenderResponseDto> createGender(@Valid @RequestBody GenderDto genderDto) {
        var createdGender = genderService.createGender(genderDto);
        GenderResponseDto responseDto = convertToResponseDto(createdGender);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GenderResponseDto> updateGender(@PathVariable Integer id, @Valid @RequestBody GenderDto genderDto) {
        var updatedGender = genderService.update(id, genderDto);
        GenderResponseDto responseDto = convertToResponseDto(updatedGender);
        return ResponseEntity.ok(responseDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGender(@PathVariable Integer id) {
        genderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<GenderResponseDto>> searchGenders(@RequestParam String searchTerm) {
        List<GenderResponseDto> genders = genderService.findBySearchTerm(searchTerm).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(genders);
    }
    
    private GenderResponseDto convertToResponseDto(sakura_arqui.sakura_backend.model.Gender gender) {
        return new GenderResponseDto(
            gender.getGenderId(),
            gender.getCode(),
            gender.getName(),
            gender.isStatus()
        );
    }
} 