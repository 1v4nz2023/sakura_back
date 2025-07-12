package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Gender;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer> {
    
    List<Gender> findByStatusTrue();
    
    Optional<Gender> findByCode(String code);
    
    Optional<Gender> findByName(String name);
    
    List<Gender> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT g FROM Gender g WHERE g.name LIKE %:searchTerm% OR g.code LIKE %:searchTerm%")
    List<Gender> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    boolean existsByCode(String code);
    
    boolean existsByName(String name);
} 