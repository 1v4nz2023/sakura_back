package sakura_arqui.sakura_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "promos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promo_id")
    private Integer promoId;
    
    @Column(name = "name", nullable = false, length = 80)
    private String name;
    
    @Column(name = "discount_pct", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPct;
    
    @Column(name = "active_from")
    private LocalDate activeFrom;
    
    @Column(name = "active_to")
    private LocalDate activeTo;
} 