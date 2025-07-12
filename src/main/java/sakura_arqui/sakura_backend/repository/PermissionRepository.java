package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Permission;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    
    List<Permission> findByStatusTrue();
    
    Optional<Permission> findByCode(String code);
    
    List<Permission> findByCodeContainingIgnoreCase(String code);
    
    @Query("SELECT p FROM Permission p WHERE p.code LIKE %:searchTerm% OR p.descripcion LIKE %:searchTerm%")
    List<Permission> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    boolean existsByCode(String code);
} 