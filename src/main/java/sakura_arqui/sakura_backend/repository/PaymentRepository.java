package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    List<Payment> findByQuotationQuotationId(Integer quotationId);
    
    List<Payment> findByMethodMethodId(Integer methodId);
    
    List<Payment> findByCreatedByUserId(Integer userId);
    
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Payment> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    
    @Query("SELECT p FROM Payment p WHERE p.quotation.patient.firstName LIKE %:searchTerm% OR p.quotation.patient.lastName LIKE %:searchTerm%")
    List<Payment> findByPatientSearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT p FROM Payment p WHERE p.status = :status AND p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByStatusAndDateRange(@Param("status") Payment.PaymentStatus status, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = :status AND p.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByStatusAndDateRange(@Param("status") Payment.PaymentStatus status, 
                                            @Param("startDate") LocalDateTime startDate, 
                                            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = :status")
    Long countByStatus(@Param("status") Payment.PaymentStatus status);
} 