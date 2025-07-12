package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.DistrictDto;
import sakura_arqui.sakura_backend.model.District;

import java.util.List;
import java.util.Optional;

public interface DistrictService {
    
    List<District> findAll();
    
    List<District> findActive();
    
    Optional<District> findById(Integer id);
    
    Optional<District> findByName(String name);
    
    District save(District district);
    
    District update(Integer id, DistrictDto districtDto);
    
    void deleteById(Integer id);
    
    List<District> findBySearchTerm(String searchTerm);
    
    District createDistrict(DistrictDto districtDto);
    
    boolean existsByName(String name);
} 