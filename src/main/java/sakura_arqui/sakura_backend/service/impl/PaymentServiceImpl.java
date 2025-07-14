package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakura_arqui.sakura_backend.dto.PaymentDto;
import sakura_arqui.sakura_backend.model.*;
import sakura_arqui.sakura_backend.repository.*;
import sakura_arqui.sakura_backend.service.PaymentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final QuotationRepository quotationRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PaymentDto createPayment(PaymentDto dto) {
        Payment payment = new Payment();
        Quotation quotation = quotationRepository.findById(dto.getQuotationId())
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
        payment.setQuotation(quotation);
        payment.setAmount(dto.getAmount());
        payment.setBalanceRemaining(dto.getBalanceRemaining());
        if (dto.getStatus() != null) {
            payment.setStatus(Payment.PaymentStatus.valueOf(dto.getStatus().toUpperCase()));
        } else {
            payment.setStatus(Payment.PaymentStatus.CONFIRMADO);
        }

        PaymentMethod method = paymentMethodRepository.findById(dto.getMethodId())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
        payment.setMethod(method);

        User createdBy = userRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        payment.setCreatedBy(createdBy);

        if (dto.getCanceledBy() != null) {
            User canceledBy = userRepository.findById(dto.getCanceledBy())
                    .orElseThrow(() -> new RuntimeException("Usuario de anulación no encontrado"));
            payment.setCanceledBy(canceledBy);
        }

        // Lógica para documento: si es RUC, usar el que viene; si no, usar el del paciente
        if (dto.getDocumentType() != null && !dto.getDocumentType().isEmpty() && dto.getDocumentNumber() != null && !dto.getDocumentNumber().isEmpty()) {
            payment.setDocumentType(dto.getDocumentType());
            payment.setDocumentNumber(dto.getDocumentNumber());
        } else {
            // Usar el documento del paciente de la cotización
            Patient patient = quotation.getPatient();
            payment.setDocumentType(patient.getDocumentType().getCode());
            payment.setDocumentNumber(patient.getDni());
        }

        payment = paymentRepository.save(payment);
        return toDto(payment);
    }

    @Override
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PaymentDto getPaymentById(Long id) {
        return paymentRepository.findById(id).map(this::toDto).orElse(null);
    }

    private PaymentDto toDto(Payment payment) {
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(payment.getPaymentId());
        dto.setQuotationId(payment.getQuotation().getQuotationId());
        dto.setMethodId(payment.getMethod().getMethodId());
        dto.setAmount(payment.getAmount());
        dto.setBalanceRemaining(payment.getBalanceRemaining());
        dto.setStatus(payment.getStatus().name());
        dto.setCreatedBy(payment.getCreatedBy().getUserId());
        dto.setPaymentDate(payment.getPaymentDate());
        if (payment.getCanceledBy() != null) {
            dto.setCanceledBy(payment.getCanceledBy().getUserId());
        }
        dto.setDocumentType(payment.getDocumentType());
        dto.setDocumentNumber(payment.getDocumentNumber());
        return dto;
    }
} 