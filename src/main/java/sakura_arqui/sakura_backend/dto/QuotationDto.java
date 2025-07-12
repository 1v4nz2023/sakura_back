package sakura_arqui.sakura_backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sakura_arqui.sakura_backend.model.Quotation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationDto {
    
    private Integer quotationId;
    
    @NotNull(message = "Patient is required")
    private Integer patientId;
    
    private Integer historyId;
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;
    
    private Quotation.QuotationStatus status = Quotation.QuotationStatus.PENDIENTE;
    
    private LocalDateTime createdAt;
    
    private List<QuotationItemDto> items;
} 