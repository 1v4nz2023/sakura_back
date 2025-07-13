package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.DocumentTypeDto;
import sakura_arqui.sakura_backend.service.DocumentTypeService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/document-types")
@RequiredArgsConstructor
public class DocumentTypeController {
    
    private final DocumentTypeService documentTypeService;
    
    @GetMapping
    public ResponseEntity<List<DocumentTypeDto>> getAllDocumentTypes() {
        List<DocumentTypeDto> documentTypes = documentTypeService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(documentTypes);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<DocumentTypeDto>> getActiveDocumentTypes() {
        List<DocumentTypeDto> documentTypes = documentTypeService.findActive().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(documentTypes);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DocumentTypeDto> getDocumentTypeById(@PathVariable Integer id) {
        return documentTypeService.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<DocumentTypeDto> getDocumentTypeByCode(@PathVariable String code) {
        return documentTypeService.findByCode(code)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<DocumentTypeDto> createDocumentType(@Valid @RequestBody DocumentTypeDto documentTypeDto) {
        var createdDocumentType = documentTypeService.createDocumentType(documentTypeDto);
        DocumentTypeDto responseDto = convertToDto(createdDocumentType);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DocumentTypeDto> updateDocumentType(@PathVariable Integer id, @Valid @RequestBody DocumentTypeDto documentTypeDto) {
        var updatedDocumentType = documentTypeService.update(id, documentTypeDto);
        DocumentTypeDto responseDto = convertToDto(updatedDocumentType);
        return ResponseEntity.ok(responseDto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentType(@PathVariable Integer id) {
        documentTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<DocumentTypeDto>> searchDocumentTypes(@RequestParam String searchTerm) {
        List<DocumentTypeDto> documentTypes = documentTypeService.findBySearchTerm(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(documentTypes);
    }
    
    private DocumentTypeDto convertToDto(sakura_arqui.sakura_backend.model.DocumentType documentType) {
        return new DocumentTypeDto(
            documentType.getDocumentTypeId(),
            documentType.getCode(),
            documentType.getName(),
            documentType.isStatus()
        );
    }
} 