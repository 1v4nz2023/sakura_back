package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.QuotationDto;
import sakura_arqui.sakura_backend.model.Quotation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuotationService {
    
    List<Quotation> findAll();
    
    Optional<Quotation> findById(Integer id);
    
    Quotation save(Quotation quotation);
    
    Quotation update(Integer id, QuotationDto quotationDto);
    
    void deleteById(Integer id);
    
    List<Quotation> findByStatus(Quotation.QuotationStatus status);
    
    List<Quotation> findByPatientId(Integer patientId);
    
    List<Quotation> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Quotation> findByPatientSearchTerm(String searchTerm);
    
    Quotation createQuotation(QuotationDto quotationDto);
    
    Quotation updateStatus(Integer id, Quotation.QuotationStatus status);
    
    BigDecimal calculateTotalAmount(Integer quotationId);
    
    Long countByStatus(Quotation.QuotationStatus status);
    
    BigDecimal sumTotalAmountByStatusAndDateRange(Quotation.QuotationStatus status, 
                                                 LocalDateTime startDate, 
                                                 LocalDateTime endDate);
} 