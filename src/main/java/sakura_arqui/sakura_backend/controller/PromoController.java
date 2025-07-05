package sakura_arqui.sakura_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.model.Promo;
import sakura_arqui.sakura_backend.repository.PromoRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/promos")
@RequiredArgsConstructor
public class PromoController {
    
    private final PromoRepository promoRepository;
    
    @GetMapping
    public ResponseEntity<List<Promo>> getAllPromos() {
        List<Promo> promos = promoRepository.findAll();
        return ResponseEntity.ok(promos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Promo> getPromoById(@PathVariable Integer id) {
        return promoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Promo> createPromo(@RequestBody Promo promo) {
        Promo createdPromo = promoRepository.save(promo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPromo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Promo> updatePromo(@PathVariable Integer id, @RequestBody Promo promo) {
        if (!promoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        promo.setPromoId(id);
        Promo updatedPromo = promoRepository.save(promo);
        return ResponseEntity.ok(updatedPromo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromo(@PathVariable Integer id) {
        if (!promoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        promoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Promo>> getActivePromos(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate currentDate) {
        if (currentDate == null) {
            currentDate = LocalDate.now();
        }
        List<Promo> promos = promoRepository.findActivePromos(currentDate);
        return ResponseEntity.ok(promos);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Promo>> searchPromos(@RequestParam String searchTerm) {
        List<Promo> promos = promoRepository.findByNameContainingIgnoreCase(searchTerm);
        return ResponseEntity.ok(promos);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<Promo>> getPromosByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Promo> promos = promoRepository.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(promos);
    }
} 