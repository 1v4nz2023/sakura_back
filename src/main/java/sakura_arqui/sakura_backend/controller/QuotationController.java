package sakura_arqui.sakura_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.QuotationDto;
import sakura_arqui.sakura_backend.model.Payment;
import sakura_arqui.sakura_backend.model.Quotation;
import sakura_arqui.sakura_backend.repository.PaymentRepository;
import sakura_arqui.sakura_backend.repository.QuotationRepository;
import sakura_arqui.sakura_backend.service.QuotationService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quotations")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;
    private final QuotationRepository quotationRepository;
    private final PaymentRepository paymentRepository;

    @PostMapping
    public ResponseEntity<QuotationDto> createQuotation(@RequestBody QuotationDto quotationDto) {
        QuotationDto created = quotationService.createQuotation(quotationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuotationWithPayments(@PathVariable Integer id) {
        Quotation quotation = quotationRepository.findById(id).orElse(null);
        if (quotation == null) {
            return ResponseEntity.notFound().build();
        }
        BigDecimal totalAmount = quotation.getTotalAmount();
        List<Payment> confirmedPayments = paymentRepository.findByQuotationAndStatus(quotation, Payment.PaymentStatus.CONFIRMADO);
        BigDecimal totalPaid = confirmedPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal balanceRemaining = totalAmount.subtract(totalPaid);
        // Si la cotización está pendiente y el saldo es 0, actualizar a PAGADA
        if (balanceRemaining.compareTo(BigDecimal.ZERO) == 0 && !quotation.getStatus().equals(Quotation.QuotationStatus.PAGADA)) {
            quotation.setStatus(Quotation.QuotationStatus.PAGADA);
            quotationRepository.save(quotation);
        }
        List<Map<String, Object>> payments = confirmedPayments.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("amount", p.getAmount());
            map.put("date", p.getPaymentDate());
            map.put("method", p.getMethod().getName());
            return map;
        }).collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("quotation_id", quotation.getQuotationId());
        response.put("total_amount", totalAmount);
        response.put("total_paid", totalPaid);
        response.put("balance_remaining", balanceRemaining);
        response.put("payments", payments);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<QuotationDto>> getAllQuotations() {
        List<QuotationDto> quotations = quotationService.getAllQuotations();
        return ResponseEntity.ok(quotations);
    }
} 