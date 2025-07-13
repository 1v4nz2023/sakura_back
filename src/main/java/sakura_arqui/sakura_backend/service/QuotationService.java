package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.QuotationDto;
import java.util.List;

public interface QuotationService {
    QuotationDto createQuotation(QuotationDto quotationDto);
    List<QuotationDto> getAllQuotations();
    QuotationDto getQuotationById(Integer id);
} 