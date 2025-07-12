package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakura_arqui.sakura_backend.dto.PermissionDto;
import sakura_arqui.sakura_backend.exception.ResourceNotFoundException;
import sakura_arqui.sakura_backend.model.Permission;
import sakura_arqui.sakura_backend.repository.PermissionRepository;
import sakura_arqui.sakura_backend.service.PermissionService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionServiceImpl implements PermissionService {
    
    private final PermissionRepository permissionRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Permission> findActive() {
        return permissionRepository.findByStatusTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Permission> findById(Integer id) {
        return permissionRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Permission> findByCode(String code) {
        return permissionRepository.findByCode(code);
    }
    
    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }
    
    @Override
    public Permission update(Integer id, PermissionDto permissionDto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + id));
        
        permission.setCode(permissionDto.getCode());
        permission.setDescripcion(permissionDto.getDescripcion());
        permission.setStatus(permissionDto.isStatus());
        
        return permissionRepository.save(permission);
    }
    
    @Override
    public void deleteById(Integer id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + id));
        
        // Soft delete - set status to false
        permission.setStatus(false);
        permissionRepository.save(permission);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Permission> findBySearchTerm(String searchTerm) {
        return permissionRepository.findBySearchTerm(searchTerm);
    }
    
    @Override
    public Permission createPermission(PermissionDto permissionDto) {
        if (permissionRepository.existsByCode(permissionDto.getCode())) {
            throw new IllegalArgumentException("Permission with code " + permissionDto.getCode() + " already exists");
        }
        
        Permission permission = new Permission();
        permission.setCode(permissionDto.getCode());
        permission.setDescripcion(permissionDto.getDescripcion());
        permission.setStatus(permissionDto.isStatus());
        
        return permissionRepository.save(permission);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return permissionRepository.existsByCode(code);
    }
} 