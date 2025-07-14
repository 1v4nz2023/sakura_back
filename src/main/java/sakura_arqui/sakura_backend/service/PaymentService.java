package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.PaymentDto;
import java.util.List;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllPayments();
    List<PaymentDto> getPaymentsByPatientId(Integer patientId);
    PaymentDto getPaymentById(Long id);
} 