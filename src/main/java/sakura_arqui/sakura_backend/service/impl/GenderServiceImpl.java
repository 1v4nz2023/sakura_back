package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakura_arqui.sakura_backend.dto.GenderDto;
import sakura_arqui.sakura_backend.exception.ResourceNotFoundException;
import sakura_arqui.sakura_backend.model.Gender;
import sakura_arqui.sakura_backend.repository.GenderRepository;
import sakura_arqui.sakura_backend.service.GenderService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GenderServiceImpl implements GenderService {
    
    private final GenderRepository genderRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<Gender> findAll() {
        return genderRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Gender> findActive() {
        return genderRepository.findByStatusTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Gender> findById(Integer id) {
        return genderRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Gender> findByCode(String code) {
        return genderRepository.findByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Gender> findByName(String name) {
        return genderRepository.findByName(name);
    }
    
    @Override
    public Gender save(Gender gender) {
        return genderRepository.save(gender);
    }
    
    @Override
    public Gender update(Integer id, GenderDto genderDto) {
        Gender gender = genderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gender not found with id: " + id));
        
        gender.setCode(genderDto.getCode());
        gender.setName(genderDto.getName());
        gender.setStatus(genderDto.isStatus());
        
        return genderRepository.save(gender);
    }
    
    @Override
    public void deleteById(Integer id) {
        Gender gender = genderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gender not found with id: " + id));
        
        // Soft delete - set status to false
        gender.setStatus(false);
        genderRepository.save(gender);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Gender> findBySearchTerm(String searchTerm) {
        return genderRepository.findBySearchTerm(searchTerm);
    }
    
    @Override
    public Gender createGender(GenderDto genderDto) {
        if (genderRepository.existsByCode(genderDto.getCode())) {
            throw new IllegalArgumentException("Gender with code " + genderDto.getCode() + " already exists");
        }
        
        if (genderRepository.existsByName(genderDto.getName())) {
            throw new IllegalArgumentException("Gender with name " + genderDto.getName() + " already exists");
        }
        
        Gender gender = new Gender();
        gender.setCode(genderDto.getCode());
        gender.setName(genderDto.getName());
        gender.setStatus(genderDto.isStatus());
        
        return genderRepository.save(gender);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return genderRepository.existsByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return genderRepository.existsByName(name);
    }
} 