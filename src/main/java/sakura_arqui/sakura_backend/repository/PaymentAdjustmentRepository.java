package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.PaymentAdjustment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentAdjustmentRepository extends JpaRepository<PaymentAdjustment, Long> {
    
    List<PaymentAdjustment> findByPaymentPaymentId(Long paymentId);
    
    List<PaymentAdjustment> findByAdjustedByUserId(Integer userId);
    
    List<PaymentAdjustment> findByAdjustedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT pa FROM PaymentAdjustment pa WHERE pa.amountAdjusted BETWEEN :minAmount AND :maxAmount")
    List<PaymentAdjustment> findByAmountAdjustedBetween(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);
    
    @Query("SELECT SUM(pa.amountAdjusted) FROM PaymentAdjustment pa WHERE pa.adjustedAt BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountAdjustedByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
} 