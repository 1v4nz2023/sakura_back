package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.ServiceDto;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceService {
    List<ServiceDto> getAllServices();
    ServiceDto getServiceById(Integer id);
    ServiceDto createService(ServiceDto serviceDto);
    ServiceDto updateService(Integer id, ServiceDto serviceDto);
    void deleteService(Integer id);
    List<ServiceDto> searchServices(String searchTerm);
    List<ServiceDto> getServicesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<ServiceDto> getServicesByMaxPrice(BigDecimal maxPrice);
    List<ServiceDto> getServicesByMinPrice(BigDecimal minPrice);
    List<ServiceDto> getServicesByCategory(Integer categoryId);
} 