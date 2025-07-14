package sakura_arqui.sakura_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.ServiceDto;
import sakura_arqui.sakura_backend.service.ServiceService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
    
    private final ServiceService serviceService;
    
    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        List<ServiceDto> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Integer id) {
        ServiceDto service = serviceService.getServiceById(id);
        if (service != null) {
            return ResponseEntity.ok(service);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<ServiceDto> createService(@RequestBody ServiceDto serviceDto) {
        ServiceDto createdService = serviceService.createService(serviceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdService);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ServiceDto> updateService(@PathVariable Integer id, @RequestBody ServiceDto serviceDto) {
        ServiceDto updatedService = serviceService.updateService(id, serviceDto);
        if (updatedService != null) {
            return ResponseEntity.ok(updatedService);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ServiceDto>> searchServices(@RequestParam String searchTerm) {
        List<ServiceDto> services = serviceService.searchServices(searchTerm);
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/price-range")
    public ResponseEntity<List<ServiceDto>> getServicesByPriceRange(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        List<ServiceDto> services = serviceService.getServicesByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/max-price/{maxPrice}")
    public ResponseEntity<List<ServiceDto>> getServicesByMaxPrice(@PathVariable BigDecimal maxPrice) {
        List<ServiceDto> services = serviceService.getServicesByMaxPrice(maxPrice);
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/min-price/{minPrice}")
    public ResponseEntity<List<ServiceDto>> getServicesByMinPrice(@PathVariable BigDecimal minPrice) {
        List<ServiceDto> services = serviceService.getServicesByMinPrice(minPrice);
        return ResponseEntity.ok(services);
    }
} 