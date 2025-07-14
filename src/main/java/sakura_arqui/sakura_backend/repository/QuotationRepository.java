package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sakura_arqui.sakura_backend.model.Quotation;

public interface QuotationRepository extends JpaRepository<Quotation, Integer> {
} 