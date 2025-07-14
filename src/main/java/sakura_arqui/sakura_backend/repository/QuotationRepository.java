package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sakura_arqui.sakura_backend.model.Quotation;
import java.util.List;

public interface QuotationRepository extends JpaRepository<Quotation, Integer> {
    List<Quotation> findByPatientPatientId(Integer patientId);
} 