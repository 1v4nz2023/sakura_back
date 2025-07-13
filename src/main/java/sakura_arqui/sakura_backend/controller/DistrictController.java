package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.DistrictDto;
import sakura_arqui.sakura_backend.service.DistrictService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
public class DistrictController {
    
    private final DistrictService districtService;
    
    @GetMapping
    public ResponseEntity<List<DistrictDto>> getAllDistricts() {
        List<DistrictDto> districts = districtService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(districts);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<DistrictDto>> getActiveDistricts() {
        List<DistrictDto> districts = districtService.findActive().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(districts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DistrictDto> getDistrictById(@PathVariable Integer id) {
        return districtService.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<DistrictDto> getDistrictByName(@PathVariable String name) {
        return districtService.findByName(name)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<DistrictDto> createDistrict(@Valid @RequestBody DistrictDto districtDto) {
        var createdDistrict = districtService.createDistrict(districtDto);
        DistrictDto responseDto = convertToDto(createdDistrict);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DistrictDto> updateDistrict(@PathVariable Integer id, @Valid @RequestBody DistrictDto districtDto) {
        var updatedDistrict = districtService.update(id, districtDto);
        DistrictDto responseDto = convertToDto(updatedDistrict);
        return ResponseEntity.ok(responseDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable Integer id) {
        districtService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<DistrictDto>> searchDistricts(@RequestParam String searchTerm) {
        List<DistrictDto> districts = districtService.findBySearchTerm(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(districts);
    }
    
    private DistrictDto convertToDto(sakura_arqui.sakura_backend.model.District district) {
        return new DistrictDto(
            district.getDistrictId(),
            district.getName(),
            district.isStatus()
        );
    }
} 