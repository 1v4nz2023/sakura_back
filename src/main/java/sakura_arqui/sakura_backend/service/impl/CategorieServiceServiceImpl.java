package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sakura_arqui.sakura_backend.dto.CategorieServiceDto;
import sakura_arqui.sakura_backend.model.CategorieService;
import sakura_arqui.sakura_backend.repository.CategorieServiceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategorieServiceServiceImpl implements sakura_arqui.sakura_backend.service.CategorieServiceService {

    private final CategorieServiceRepository categorieServiceRepository;

    @Override
    public List<CategorieServiceDto> getAllCategories() {
        return categorieServiceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategorieServiceDto getCategoryById(Integer id) {
        return categorieServiceRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public CategorieServiceDto createCategory(CategorieServiceDto categoryDto) {
        CategorieService category = convertToEntity(categoryDto);
        CategorieService savedCategory = categorieServiceRepository.save(category);
        return convertToDto(savedCategory);
    }

    @Override
    public CategorieServiceDto updateCategory(Integer id, CategorieServiceDto categoryDto) {
        if (!categorieServiceRepository.existsById(id)) {
            return null;
        }
        CategorieService category = convertToEntity(categoryDto);
        category.setCategorieServiceId(id);
        CategorieService updatedCategory = categorieServiceRepository.save(category);
        return convertToDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        categorieServiceRepository.deleteById(id);
    }

    private CategorieServiceDto convertToDto(CategorieService category) {
        CategorieServiceDto dto = new CategorieServiceDto();
        dto.setCategorieServiceId(category.getCategorieServiceId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setStatus(category.isStatus());
        
        return dto;
    }

    private CategorieService convertToEntity(CategorieServiceDto dto) {
        CategorieService category = new CategorieService();
        category.setCategorieServiceId(dto.getCategorieServiceId());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setStatus(dto.isStatus());
        
        return category;
    }
} 