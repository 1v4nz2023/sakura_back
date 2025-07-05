package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.QuotationItem;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface QuotationItemRepository extends JpaRepository<QuotationItem, Integer> {
    
    List<QuotationItem> findByQuotationQuotationId(Integer quotationId);
    
    List<QuotationItem> findByServiceServiceId(Integer serviceId);
    
    @Query("SELECT qi FROM QuotationItem qi WHERE qi.quotation.quotationId = :quotationId ORDER BY qi.itemId")
    List<QuotationItem> findByQuotationIdOrderByItemId(@Param("quotationId") Integer quotationId);
    
    @Query("SELECT SUM(qi.subtotal) FROM QuotationItem qi WHERE qi.quotation.quotationId = :quotationId")
    BigDecimal sumSubtotalByQuotationId(@Param("quotationId") Integer quotationId);
} 