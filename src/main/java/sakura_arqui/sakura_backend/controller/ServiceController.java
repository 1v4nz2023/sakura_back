package sakura_arqui.sakura_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.model.Service;
import sakura_arqui.sakura_backend.repository.ServiceRepository;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
    
    private final ServiceRepository serviceRepository;
    
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Integer id) {
        return serviceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        Service createdService = serviceRepository.save(service);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdService);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable Integer id, @RequestBody Service service) {
        if (!serviceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        service.setServiceId(id);
        Service updatedService = serviceRepository.save(service);
        return ResponseEntity.ok(updatedService);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        if (!serviceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        serviceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Service>> searchServices(@RequestParam String searchTerm) {
        List<Service> services = serviceRepository.findBySearchTerm(searchTerm);
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/price-range")
    public ResponseEntity<List<Service>> getServicesByPriceRange(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        List<Service> services = serviceRepository.findByBasePriceBetween(minPrice, maxPrice);
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/max-price/{maxPrice}")
    public ResponseEntity<List<Service>> getServicesByMaxPrice(@PathVariable BigDecimal maxPrice) {
        List<Service> services = serviceRepository.findByBasePriceLessThanEqual(maxPrice);
        return ResponseEntity.ok(services);
    }
    
    @GetMapping("/min-price/{minPrice}")
    public ResponseEntity<List<Service>> getServicesByMinPrice(@PathVariable BigDecimal minPrice) {
        List<Service> services = serviceRepository.findByBasePriceGreaterThanEqualOrderByBasePriceAsc(minPrice);
        return ResponseEntity.ok(services);
    }
} 