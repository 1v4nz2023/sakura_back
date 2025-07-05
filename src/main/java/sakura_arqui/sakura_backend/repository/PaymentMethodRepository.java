package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.PaymentMethod;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    
    Optional<PaymentMethod> findByName(String name);
    
    boolean existsByName(String name);
} 