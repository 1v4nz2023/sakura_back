package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.DistrictDto;
import sakura_arqui.sakura_backend.model.District;
import sakura_arqui.sakura_backend.service.DistrictService;

import java.util.List;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
public class DistrictController {
    
    private final DistrictService districtService;
    
    @GetMapping
    public ResponseEntity<List<District>> getAllDistricts() {
        List<District> districts = districtService.findAll();
        return ResponseEntity.ok(districts);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<District>> getActiveDistricts() {
        List<District> districts = districtService.findActive();
        return ResponseEntity.ok(districts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<District> getDistrictById(@PathVariable Integer id) {
        return districtService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<District> getDistrictByName(@PathVariable String name) {
        return districtService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<District> createDistrict(@Valid @RequestBody DistrictDto districtDto) {
        District createdDistrict = districtService.createDistrict(districtDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDistrict);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<District> updateDistrict(@PathVariable Integer id, @Valid @RequestBody DistrictDto districtDto) {
        District updatedDistrict = districtService.update(id, districtDto);
        return ResponseEntity.ok(updatedDistrict);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable Integer id) {
        districtService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<District>> searchDistricts(@RequestParam String searchTerm) {
        List<District> districts = districtService.findBySearchTerm(searchTerm);
        return ResponseEntity.ok(districts);
    }
} 