package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.District;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, Integer> {
    
    List<District> findByStatusTrue();
    
    Optional<District> findByName(String name);
    
    List<District> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT d FROM District d WHERE d.name LIKE %:searchTerm%")
    List<District> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    boolean existsByName(String name);
} 