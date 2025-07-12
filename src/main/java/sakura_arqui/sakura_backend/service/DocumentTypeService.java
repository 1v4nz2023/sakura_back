package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.DocumentTypeDto;
import sakura_arqui.sakura_backend.model.DocumentType;

import java.util.List;
import java.util.Optional;

public interface DocumentTypeService {
    
    List<DocumentType> findAll();
    
    List<DocumentType> findActive();
    
    Optional<DocumentType> findById(Integer id);
    
    Optional<DocumentType> findByCode(String code);
    
    DocumentType save(DocumentType documentType);
    
    DocumentType update(Integer id, DocumentTypeDto documentTypeDto);
    
    void deleteById(Integer id);
    
    List<DocumentType> findBySearchTerm(String searchTerm);
    
    DocumentType createDocumentType(DocumentTypeDto documentTypeDto);
    
    boolean existsByCode(String code);
} 