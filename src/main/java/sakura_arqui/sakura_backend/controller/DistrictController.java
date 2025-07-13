package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.DistrictDto;
import sakura_arqui.sakura_backend.dto.DistrictResponseDto;
import sakura_arqui.sakura_backend.service.DistrictService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
public class DistrictController {
    
    private final DistrictService districtService;
    
    @GetMapping
    public ResponseEntity<List<DistrictResponseDto>> getAllDistricts() {
        List<DistrictResponseDto> districts = districtService.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(districts);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<DistrictResponseDto>> getActiveDistricts() {
        List<DistrictResponseDto> districts = districtService.findActive().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(districts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DistrictResponseDto> getDistrictById(@PathVariable Integer id) {
        return districtService.findById(id)
                .map(this::convertToResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<DistrictResponseDto> getDistrictByName(@PathVariable String name) {
        return districtService.findByName(name)
                .map(this::convertToResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<DistrictResponseDto> createDistrict(@Valid @RequestBody DistrictDto districtDto) {
        var createdDistrict = districtService.createDistrict(districtDto);
        DistrictResponseDto responseDto = convertToResponseDto(createdDistrict);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DistrictResponseDto> updateDistrict(@PathVariable Integer id, @Valid @RequestBody DistrictDto districtDto) {
        var updatedDistrict = districtService.update(id, districtDto);
        DistrictResponseDto responseDto = convertToResponseDto(updatedDistrict);
        return ResponseEntity.ok(responseDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable Integer id) {
        districtService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<DistrictResponseDto>> searchDistricts(@RequestParam String searchTerm) {
        List<DistrictResponseDto> districts = districtService.findBySearchTerm(searchTerm).stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(districts);
    }
    
    private DistrictResponseDto convertToResponseDto(sakura_arqui.sakura_backend.model.District district) {
        return new DistrictResponseDto(
            district.getDistrictId(),
            district.getName(),
            district.isStatus()
        );
    }
} 