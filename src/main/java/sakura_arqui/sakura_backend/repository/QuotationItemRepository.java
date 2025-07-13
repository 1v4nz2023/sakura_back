package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sakura_arqui.sakura_backend.model.Quotation;
import sakura_arqui.sakura_backend.model.QuotationItem;

import java.util.List;

public interface QuotationItemRepository extends JpaRepository<QuotationItem, Integer> {
    List<QuotationItem> findByQuotation(Quotation quotation);
} 