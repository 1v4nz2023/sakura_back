package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Receipt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    
    Optional<Receipt> findByReceiptNumber(String receiptNumber);
    
    List<Receipt> findByReceiptType(Receipt.ReceiptType receiptType);
    
    List<Receipt> findByPaymentPaymentId(Long paymentId);
    
    @Query("SELECT r FROM Receipt r WHERE r.generatedAt BETWEEN :startDate AND :endDate")
    List<Receipt> findByGeneratedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT r FROM Receipt r WHERE r.payment.quotation.patient.firstName LIKE %:searchTerm% OR r.payment.quotation.patient.lastName LIKE %:searchTerm%")
    List<Receipt> findByPatientSearchTerm(@Param("searchTerm") String searchTerm);
    
    boolean existsByReceiptNumber(String receiptNumber);
} 