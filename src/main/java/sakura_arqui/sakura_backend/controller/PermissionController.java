package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.PermissionDto;
import sakura_arqui.sakura_backend.model.Permission;
import sakura_arqui.sakura_backend.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    
    private final PermissionService permissionService;
    
    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.findAll();
        return ResponseEntity.ok(permissions);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Permission>> getActivePermissions() {
        List<Permission> permissions = permissionService.findActive();
        return ResponseEntity.ok(permissions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Integer id) {
        return permissionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Permission> getPermissionByCode(@PathVariable String code) {
        return permissionService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody PermissionDto permissionDto) {
        Permission createdPermission = permissionService.createPermission(permissionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(@PathVariable Integer id, @Valid @RequestBody PermissionDto permissionDto) {
        Permission updatedPermission = permissionService.update(id, permissionDto);
        return ResponseEntity.ok(updatedPermission);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Integer id) {
        permissionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Permission>> searchPermissions(@RequestParam String searchTerm) {
        List<Permission> permissions = permissionService.findBySearchTerm(searchTerm);
        return ResponseEntity.ok(permissions);
    }
} 