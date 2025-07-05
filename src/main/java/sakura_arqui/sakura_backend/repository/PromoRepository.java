package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Promo;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PromoRepository extends JpaRepository<Promo, Integer> {
    
    @Query("SELECT p FROM Promo p WHERE (p.activeFrom IS NULL OR p.activeFrom <= :currentDate) AND (p.activeTo IS NULL OR p.activeTo >= :currentDate)")
    List<Promo> findActivePromos(@Param("currentDate") LocalDate currentDate);
    
    List<Promo> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT p FROM Promo p WHERE p.activeFrom BETWEEN :startDate AND :endDate OR p.activeTo BETWEEN :startDate AND :endDate")
    List<Promo> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
} 