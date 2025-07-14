package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.CategorieServiceDto;

import java.util.List;

public interface CategorieServiceService {
    List<CategorieServiceDto> getAllCategories();
    CategorieServiceDto getCategoryById(Integer id);
    CategorieServiceDto createCategory(CategorieServiceDto categoryDto);
    CategorieServiceDto updateCategory(Integer id, CategorieServiceDto categoryDto);
    void deleteCategory(Integer id);
} 