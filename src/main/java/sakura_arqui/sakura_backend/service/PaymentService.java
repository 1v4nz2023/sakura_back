package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.PaymentDto;
import java.util.List;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    List<PaymentDto> getAllPayments();
    PaymentDto getPaymentById(Long id);
} 