package sakura_arqui.sakura_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.model.PaymentMethod;
import sakura_arqui.sakura_backend.repository.PaymentMethodRepository;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {
    
    private final PaymentMethodRepository paymentMethodRepository;
    
    @GetMapping
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        List<PaymentMethod> methods = paymentMethodRepository.findAll();
        return ResponseEntity.ok(methods);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable Integer id) {
        return paymentMethodRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<PaymentMethod> getPaymentMethodByName(@PathVariable String name) {
        return paymentMethodRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<PaymentMethod> createPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        if (paymentMethodRepository.existsByName(paymentMethod.getName())) {
            return ResponseEntity.badRequest().build();
        }
        PaymentMethod createdMethod = paymentMethodRepository.save(paymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMethod);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethod> updatePaymentMethod(@PathVariable Integer id, @RequestBody PaymentMethod paymentMethod) {
        if (!paymentMethodRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paymentMethod.setMethodId(id);
        PaymentMethod updatedMethod = paymentMethodRepository.save(paymentMethod);
        return ResponseEntity.ok(updatedMethod);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Integer id) {
        if (!paymentMethodRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        paymentMethodRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> checkNameExists(@PathVariable String name) {
        boolean exists = paymentMethodRepository.existsByName(name);
        return ResponseEntity.ok(exists);
    }
} 