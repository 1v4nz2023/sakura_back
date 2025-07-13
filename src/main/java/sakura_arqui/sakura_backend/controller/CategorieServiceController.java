package sakura_arqui.sakura_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.CategorieServiceDto;
import sakura_arqui.sakura_backend.dto.ServiceDto;
import sakura_arqui.sakura_backend.service.CategorieServiceService;
import sakura_arqui.sakura_backend.service.ServiceService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategorieServiceController {
    
    private final CategorieServiceService categorieServiceService;
    private final ServiceService serviceService;
    
    @GetMapping
    public ResponseEntity<List<CategorieServiceDto>> getAllCategories() {
        try {
            log.info("Fetching all categories...");
            List<CategorieServiceDto> categories = categorieServiceService.getAllCategories();
            log.info("Found {} categories", categories.size());
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            log.error("Error fetching categories: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategorieServiceDto> getCategoryById(@PathVariable Integer id) {
        try {
            log.info("Fetching category with id: {}", id);
            CategorieServiceDto category = categorieServiceService.getCategoryById(id);
            if (category != null) {
                return ResponseEntity.ok(category);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching category with id {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}/services")
    public ResponseEntity<List<ServiceDto>> getServicesByCategory(@PathVariable Integer id) {
        try {
            log.info("Fetching services for category id: {}", id);
            List<ServiceDto> services = serviceService.getServicesByCategory(id);
            log.info("Found {} services for category {}", services.size(), id);
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            log.error("Error fetching services for category {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<CategorieServiceDto> createCategory(@RequestBody CategorieServiceDto categoryDto) {
        try {
            log.info("Creating new category: {}", categoryDto.getName());
            CategorieServiceDto createdCategory = categorieServiceService.createCategory(categoryDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (Exception e) {
            log.error("Error creating category: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CategorieServiceDto> updateCategory(@PathVariable Integer id, @RequestBody CategorieServiceDto categoryDto) {
        try {
            log.info("Updating category with id: {}", id);
            CategorieServiceDto updatedCategory = categorieServiceService.updateCategory(id, categoryDto);
            if (updatedCategory != null) {
                return ResponseEntity.ok(updatedCategory);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating category with id {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        try {
            log.info("Deleting category with id: {}", id);
            categorieServiceService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting category with id {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 