package sakura_arqui.sakura_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {
    private Integer serviceId;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private boolean status;
    private Integer categoryId;
    private String categoryName;
} 