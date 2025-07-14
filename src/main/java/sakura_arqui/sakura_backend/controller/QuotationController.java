package sakura_arqui.sakura_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.QuotationDto;
import sakura_arqui.sakura_backend.service.QuotationService;

import java.util.List;

@RestController
@RequestMapping("/api/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    @PostMapping
    public ResponseEntity<QuotationDto> createQuotation(@RequestBody QuotationDto quotationDto) {
        QuotationDto created = quotationService.createQuotation(quotationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<QuotationDto>> getAllQuotations() {
        List<QuotationDto> quotations = quotationService.getAllQuotations();
        return ResponseEntity.ok(quotations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotationDto> getQuotationById(@PathVariable Integer id) {
        QuotationDto quotation = quotationService.getQuotationById(id);
        if (quotation != null) {
            return ResponseEntity.ok(quotation);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<QuotationDto>> getQuotationsByPatientId(@PathVariable Integer patientId) {
        List<QuotationDto> quotations = quotationService.getQuotationsByPatientId(patientId);
        return ResponseEntity.ok(quotations);
    }
} 