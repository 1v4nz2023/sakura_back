package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Quotation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Integer> {
    
    List<Quotation> findByStatus(Quotation.QuotationStatus status);
    
    List<Quotation> findByPatientPatientId(Integer patientId);
    
    List<Quotation> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    
    List<Quotation> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT q FROM Quotation q WHERE q.patient.firstName LIKE %:searchTerm% OR q.patient.lastName LIKE %:searchTerm% OR q.patient.dni LIKE %:searchTerm%")
    List<Quotation> findByPatientSearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT q FROM Quotation q WHERE q.status = :status AND q.createdAt >= :startDate")
    List<Quotation> findByStatusAndCreatedAtAfter(@Param("status") Quotation.QuotationStatus status, @Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT COUNT(q) FROM Quotation q WHERE q.status = :status")
    Long countByStatus(@Param("status") Quotation.QuotationStatus status);
    
    @Query("SELECT SUM(q.totalAmount) FROM Quotation q WHERE q.status = :status AND q.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalAmountByStatusAndDateRange(@Param("status") Quotation.QuotationStatus status, 
                                                  @Param("startDate") LocalDateTime startDate, 
                                                  @Param("endDate") LocalDateTime endDate);
} 