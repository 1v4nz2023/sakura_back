package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.DocumentType;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Integer> {
    
    List<DocumentType> findByStatusTrue();
    
    Optional<DocumentType> findByCode(String code);
    
    List<DocumentType> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT dt FROM DocumentType dt WHERE dt.name LIKE %:searchTerm% OR dt.code LIKE %:searchTerm%")
    List<DocumentType> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    boolean existsByCode(String code);
} 