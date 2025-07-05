package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Service;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    
    List<Service> findByNameContainingIgnoreCase(String name);
    
    List<Service> findByBasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Service> findByBasePriceLessThanEqual(BigDecimal maxPrice);
    
    @Query("SELECT s FROM Service s WHERE s.name LIKE %:searchTerm% OR s.description LIKE %:searchTerm%")
    List<Service> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT s FROM Service s WHERE s.basePrice >= :minPrice ORDER BY s.basePrice ASC")
    List<Service> findByBasePriceGreaterThanEqualOrderByBasePriceAsc(@Param("minPrice") BigDecimal minPrice);
} 