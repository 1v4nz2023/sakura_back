package sakura_arqui.sakura_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long paymentId;
    private Integer quotationId;
    private Integer methodId;
    private BigDecimal amount;
    private BigDecimal balanceRemaining;
    private LocalDateTime paymentDate;
    private String status; // CONFIRMADO, PENDIENTE, ANULADO
    private Integer createdBy;
    private Integer canceledBy;
    private String documentType; // "DNI" o "RUC"
    private String documentNumber;
} 