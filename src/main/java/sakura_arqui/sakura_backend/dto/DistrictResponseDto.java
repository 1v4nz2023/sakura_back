package sakura_arqui.sakura_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictResponseDto {
    private Integer districtId;
    private String name;
    private boolean status;
} 