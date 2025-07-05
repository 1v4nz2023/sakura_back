package sakura_arqui.sakura_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_adjustments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAdjustment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adjustment_id")
    private Long adjustmentId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;
    
    @Column(name = "amount_adjusted", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountAdjusted;
    
    @Column(name = "reason", nullable = false, length = 255)
    private String reason;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjusted_by", nullable = false)
    private User adjustedBy;
    
    @CreationTimestamp
    @Column(name = "adjusted_at", updatable = false)
    private LocalDateTime adjustedAt;
} 