package sakura_arqui.sakura_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.model.CategorieService;
import sakura_arqui.sakura_backend.repository.CategorieServiceRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategorieServiceController {
    
    private final CategorieServiceRepository categorieServiceRepository;
    
    @GetMapping
    public ResponseEntity<List<CategorieService>> getAllCategories() {
        List<CategorieService> categories = categorieServiceRepository.findAll();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategorieService> getCategoryById(@PathVariable Integer id) {
        return categorieServiceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<CategorieService> getCategoryByName(@PathVariable String name) {
        return categorieServiceRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<CategorieService> createCategory(@RequestBody CategorieService category) {
        CategorieService createdCategory = categorieServiceRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CategorieService> updateCategory(@PathVariable Integer id, @RequestBody CategorieService category) {
        if (!categorieServiceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        category.setCategorieServiceId(id);
        CategorieService updatedCategory = categorieServiceRepository.save(category);
        return ResponseEntity.ok(updatedCategory);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        if (!categorieServiceRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categorieServiceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<CategorieService>> getActiveCategories() {
        List<CategorieService> activeCategories = categorieServiceRepository.findByStatusTrue();
        return ResponseEntity.ok(activeCategories);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CategorieService>> searchCategories(@RequestParam String searchTerm) {
        List<CategorieService> categories = categorieServiceRepository.findByNameContainingIgnoreCase(searchTerm);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getCategoryCount() {
        long totalCount = categorieServiceRepository.count();
        long activeCount = categorieServiceRepository.countByStatusTrue();
        
        Map<String, Object> response = Map.of(
            "totalCategories", totalCount,
            "activeCategories", activeCount,
            "inactiveCategories", totalCount - activeCount
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<CategorieService> updateCategoryStatus(
            @PathVariable Integer id, 
            @RequestParam boolean status) {
        return categorieServiceRepository.findById(id)
                .map(category -> {
                    category.setStatus(status);
                    CategorieService updatedCategory = categorieServiceRepository.save(category);
                    return ResponseEntity.ok(updatedCategory);
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 