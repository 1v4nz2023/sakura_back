package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sakura_arqui.sakura_backend.dto.ServiceDto;
import sakura_arqui.sakura_backend.model.CategorieService;
import sakura_arqui.sakura_backend.model.ServiceModel;
import sakura_arqui.sakura_backend.repository.CategorieServiceRepository;
import sakura_arqui.sakura_backend.repository.ServiceRepository;
import sakura_arqui.sakura_backend.service.ServiceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final CategorieServiceRepository categorieServiceRepository;

    @Override
    public List<ServiceDto> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceDto getServiceById(Integer id) {
        return serviceRepository.findById(id)
                .map(this::convertToDto)
                .orElse(null);
    }

    @Override
    public ServiceDto createService(ServiceDto serviceDto) {
        ServiceModel service = convertToEntity(serviceDto);
        ServiceModel savedService = serviceRepository.save(service);
        return convertToDto(savedService);
    }

    @Override
    public ServiceDto updateService(Integer id, ServiceDto serviceDto) {
        if (!serviceRepository.existsById(id)) {
            return null;
        }
        ServiceModel service = convertToEntity(serviceDto);
        service.setServiceId(id);
        ServiceModel updatedService = serviceRepository.save(service);
        return convertToDto(updatedService);
    }

    @Override
    public void deleteService(Integer id) {
        serviceRepository.deleteById(id);
    }

    @Override
    public List<ServiceDto> searchServices(String searchTerm) {
        return serviceRepository.findBySearchTerm(searchTerm).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDto> getServicesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return serviceRepository.findByBasePriceBetween(minPrice, maxPrice).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDto> getServicesByMaxPrice(BigDecimal maxPrice) {
        return serviceRepository.findByBasePriceLessThanEqual(maxPrice).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDto> getServicesByMinPrice(BigDecimal minPrice) {
        return serviceRepository.findByBasePriceGreaterThanEqualOrderByBasePriceAsc(minPrice).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDto> getServicesByCategory(Integer categoryId) {
        return serviceRepository.findAll().stream()
                .filter(service -> service.getCategory() != null && 
                        service.getCategory().getCategorieServiceId().equals(categoryId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ServiceDto convertToDto(ServiceModel service) {
        ServiceDto dto = new ServiceDto();
        dto.setServiceId(service.getServiceId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setBasePrice(service.getBasePrice());
        dto.setStatus(service.isStatus());
        
        if (service.getCategory() != null) {
            dto.setCategoryId(service.getCategory().getCategorieServiceId());
            dto.setCategoryName(service.getCategory().getName());
        }
        
        return dto;
    }

    private ServiceModel convertToEntity(ServiceDto dto) {
        ServiceModel service = new ServiceModel();
        service.setServiceId(dto.getServiceId());
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setBasePrice(dto.getBasePrice());
        service.setStatus(dto.isStatus());
        
        if (dto.getCategoryId() != null) {
            CategorieService category = categorieServiceRepository.findById(dto.getCategoryId())
                    .orElse(null);
            service.setCategory(category);
        }
        
        return service;
    }
} 