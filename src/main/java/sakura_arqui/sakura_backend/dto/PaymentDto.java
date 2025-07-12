package sakura_arqui.sakura_backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sakura_arqui.sakura_backend.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    
    private Long paymentId;
    
    @NotNull(message = "Quotation is required")
    private Integer quotationId;
    
    @NotNull(message = "Payment method is required")
    private Integer methodId;
    
    // Removed promoId field
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Balance remaining is required")
    @DecimalMin(value = "0.0", message = "Balance remaining must be 0 or greater")
    private BigDecimal balanceRemaining;
    
    private LocalDateTime paymentDate;
    
    private Payment.PaymentStatus status = Payment.PaymentStatus.CONFIRMADO;
    
    @NotNull(message = "Created by user is required")
    private Integer createdBy;
    
    private Integer canceledBy;
    
    // Additional fields for display
    private String patientName;
    private String paymentMethodName;
    // Removed promoName field
} 