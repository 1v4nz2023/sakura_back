package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakura_arqui.sakura_backend.dto.DistrictDto;
import sakura_arqui.sakura_backend.exception.ResourceNotFoundException;
import sakura_arqui.sakura_backend.model.District;
import sakura_arqui.sakura_backend.repository.DistrictRepository;
import sakura_arqui.sakura_backend.service.DistrictService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DistrictServiceImpl implements DistrictService {
    
    private final DistrictRepository districtRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<District> findAll() {
        return districtRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<District> findActive() {
        return districtRepository.findByStatusTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<District> findById(Integer id) {
        return districtRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<District> findByName(String name) {
        return districtRepository.findByName(name);
    }
    
    @Override
    public District save(District district) {
        return districtRepository.save(district);
    }
    
    @Override
    public District update(Integer id, DistrictDto districtDto) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with id: " + id));
        
        district.setName(districtDto.getName());
        district.setStatus(districtDto.isStatus());
        
        return districtRepository.save(district);
    }
    
    @Override
    public void deleteById(Integer id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("District not found with id: " + id));
        
        // Soft delete - set status to false
        district.setStatus(false);
        districtRepository.save(district);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<District> findBySearchTerm(String searchTerm) {
        return districtRepository.findBySearchTerm(searchTerm);
    }
    
    @Override
    public District createDistrict(DistrictDto districtDto) {
        if (districtRepository.existsByName(districtDto.getName())) {
            throw new IllegalArgumentException("District with name " + districtDto.getName() + " already exists");
        }
        
        District district = new District();
        district.setName(districtDto.getName());
        district.setStatus(districtDto.isStatus());
        
        return districtRepository.save(district);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return districtRepository.existsByName(name);
    }
} 