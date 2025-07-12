package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakura_arqui.sakura_backend.dto.DocumentTypeDto;
import sakura_arqui.sakura_backend.exception.ResourceNotFoundException;
import sakura_arqui.sakura_backend.model.DocumentType;
import sakura_arqui.sakura_backend.repository.DocumentTypeRepository;
import sakura_arqui.sakura_backend.service.DocumentTypeService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentTypeServiceImpl implements DocumentTypeService {
    
    private final DocumentTypeRepository documentTypeRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentType> findAll() {
        return documentTypeRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentType> findActive() {
        return documentTypeRepository.findByStatusTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentType> findById(Integer id) {
        return documentTypeRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentType> findByCode(String code) {
        return documentTypeRepository.findByCode(code);
    }
    
    @Override
    public DocumentType save(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }
    
    @Override
    public DocumentType update(Integer id, DocumentTypeDto documentTypeDto) {
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentType not found with id: " + id));
        
        documentType.setCode(documentTypeDto.getCode());
        documentType.setName(documentTypeDto.getName());
        documentType.setStatus(documentTypeDto.isStatus());
        
        return documentTypeRepository.save(documentType);
    }
    
    @Override
    public void deleteById(Integer id) {
        DocumentType documentType = documentTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentType not found with id: " + id));
        
        // Soft delete - set status to false
        documentType.setStatus(false);
        documentTypeRepository.save(documentType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentType> findBySearchTerm(String searchTerm) {
        return documentTypeRepository.findBySearchTerm(searchTerm);
    }
    
    @Override
    public DocumentType createDocumentType(DocumentTypeDto documentTypeDto) {
        if (documentTypeRepository.existsByCode(documentTypeDto.getCode())) {
            throw new IllegalArgumentException("DocumentType with code " + documentTypeDto.getCode() + " already exists");
        }
        
        DocumentType documentType = new DocumentType();
        documentType.setCode(documentTypeDto.getCode());
        documentType.setName(documentTypeDto.getName());
        documentType.setStatus(documentTypeDto.isStatus());
        
        return documentTypeRepository.save(documentType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return documentTypeRepository.existsByCode(code);
    }
} 