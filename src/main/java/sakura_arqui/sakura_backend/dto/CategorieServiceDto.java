package sakura_arqui.sakura_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieServiceDto {
    private Integer categorieServiceId;
    private String name;
    private String description;
    private boolean status;
    // Removed services field to avoid circular references
} 