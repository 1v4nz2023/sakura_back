package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.DocumentTypeDto;
import sakura_arqui.sakura_backend.model.DocumentType;
import sakura_arqui.sakura_backend.service.DocumentTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/document-types")
@RequiredArgsConstructor
public class DocumentTypeController {
    
    private final DocumentTypeService documentTypeService;
    
    @GetMapping
    public ResponseEntity<List<DocumentType>> getAllDocumentTypes() {
        List<DocumentType> documentTypes = documentTypeService.findAll();
        return ResponseEntity.ok(documentTypes);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<DocumentType>> getActiveDocumentTypes() {
        List<DocumentType> documentTypes = documentTypeService.findActive();
        return ResponseEntity.ok(documentTypes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DocumentType> getDocumentTypeById(@PathVariable Integer id) {
        return documentTypeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<DocumentType> getDocumentTypeByCode(@PathVariable String code) {
        return documentTypeService.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<DocumentType> createDocumentType(@Valid @RequestBody DocumentTypeDto documentTypeDto) {
        DocumentType createdDocumentType = documentTypeService.createDocumentType(documentTypeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDocumentType);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DocumentType> updateDocumentType(@PathVariable Integer id, @Valid @RequestBody DocumentTypeDto documentTypeDto) {
        DocumentType updatedDocumentType = documentTypeService.update(id, documentTypeDto);
        return ResponseEntity.ok(updatedDocumentType);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentType(@PathVariable Integer id) {
        documentTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<DocumentType>> searchDocumentTypes(@RequestParam String searchTerm) {
        List<DocumentType> documentTypes = documentTypeService.findBySearchTerm(searchTerm);
        return ResponseEntity.ok(documentTypes);
    }
} 