package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.PermissionDto;
import sakura_arqui.sakura_backend.model.Permission;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    
    List<Permission> findAll();
    
    List<Permission> findActive();
    
    Optional<Permission> findById(Integer id);
    
    Optional<Permission> findByCode(String code);
    
    Permission save(Permission permission);
    
    Permission update(Integer id, PermissionDto permissionDto);
    
    void deleteById(Integer id);
    
    List<Permission> findBySearchTerm(String searchTerm);
    
    Permission createPermission(PermissionDto permissionDto);
    
    boolean existsByCode(String code);
} 