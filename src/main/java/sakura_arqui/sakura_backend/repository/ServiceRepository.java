package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.ServiceModel;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceModel, Integer> {
    
    List<ServiceModel> findByNameContainingIgnoreCase(String name);
    
    List<ServiceModel> findByBasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<ServiceModel> findByBasePriceLessThanEqual(BigDecimal maxPrice);
    
    @Query("SELECT s FROM ServiceModel s WHERE s.name LIKE %:searchTerm% OR s.description LIKE %:searchTerm%")
    List<ServiceModel> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT s FROM ServiceModel s WHERE s.basePrice >= :minPrice ORDER BY s.basePrice ASC")
    List<ServiceModel> findByBasePriceGreaterThanEqualOrderByBasePriceAsc(@Param("minPrice") BigDecimal minPrice);
} 