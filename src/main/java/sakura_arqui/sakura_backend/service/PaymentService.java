package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.PaymentDto;
import sakura_arqui.sakura_backend.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    
    List<Payment> findAll();
    
    Optional<Payment> findById(Long id);
    
    Payment save(Payment payment);
    
    Payment update(Long id, PaymentDto paymentDto);
    
    void deleteById(Long id);
    
    List<Payment> findByStatus(Payment.PaymentStatus status);
    
    List<Payment> findByQuotationId(Integer quotationId);
    
    List<Payment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Payment> findByPatientSearchTerm(String searchTerm);
    
    Payment createPayment(PaymentDto paymentDto);
    
    Payment updateStatus(Long id, Payment.PaymentStatus status);
    
    Payment cancelPayment(Long id, Integer canceledByUserId, String reason);
    
    BigDecimal sumAmountByStatusAndDateRange(Payment.PaymentStatus status, 
                                            LocalDateTime startDate, 
                                            LocalDateTime endDate);
    
    Long countByStatus(Payment.PaymentStatus status);
    
    BigDecimal calculateBalanceRemaining(Integer quotationId);
} 