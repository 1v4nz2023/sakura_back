package sakura_arqui.sakura_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long receiptId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;
    
    @Column(name = "receipt_number", nullable = false, unique = true, length = 30)
    private String receiptNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "receipt_type", nullable = false)
    private ReceiptType receiptType;
    
    @CreationTimestamp
    @Column(name = "generated_at", updatable = false)
    private LocalDateTime generatedAt;
    
    public enum ReceiptType {
        BOLETA, FACTURA
    }
} 