package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.GenderDto;
import sakura_arqui.sakura_backend.model.Gender;

import java.util.List;
import java.util.Optional;

public interface GenderService {
    
    List<Gender> findAll();
    
    List<Gender> findActive();
    
    Optional<Gender> findById(Integer id);
    
    Optional<Gender> findByCode(String code);
    
    Optional<Gender> findByName(String name);
    
    Gender save(Gender gender);
    
    Gender update(Integer id, GenderDto genderDto);
    
    void deleteById(Integer id);
    
    List<Gender> findBySearchTerm(String searchTerm);
    
    Gender createGender(GenderDto genderDto);
    
    boolean existsByCode(String code);
    
    boolean existsByName(String name);
} 