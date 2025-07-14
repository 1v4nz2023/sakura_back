package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakura_arqui.sakura_backend.dto.QuotationDto;
import sakura_arqui.sakura_backend.dto.QuotationItemDto;
import sakura_arqui.sakura_backend.model.*;
import sakura_arqui.sakura_backend.repository.*;
import sakura_arqui.sakura_backend.service.QuotationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final QuotationItemRepository quotationItemRepository;
    private final PatientRepository patientRepository;
    private final ServiceRepository serviceRepository;
    private final ClinicalHistoryRepository clinicalHistoryRepository;

    @Override
    @Transactional
    public QuotationDto createQuotation(QuotationDto dto) {
        Quotation quotation = new Quotation();
        quotation.setTotalAmount(dto.getTotalAmount());
        quotation.setStatus(dto.getStatus() != null ? dto.getStatus() : Quotation.QuotationStatus.PENDIENTE);

        // Relacionar paciente
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        quotation.setPatient(patient);

        // Relacionar historial clínico si viene
        if (dto.getHistoryId() != null) {
            ClinicalHistory history = clinicalHistoryRepository.findById(dto.getHistoryId())
                    .orElseThrow(() -> new RuntimeException("Historial clínico no encontrado"));
            quotation.setHistory(history);
        }

        // Guardar cabecera primero (para tener ID)
        quotation = quotationRepository.save(quotation);
        final Quotation savedQuotation = quotation;

        // Guardar items
        if (dto.getItems() != null) {
            List<QuotationItem> items = dto.getItems().stream().map(itemDto -> {
                QuotationItem item = new QuotationItem();
                item.setQuotation(savedQuotation);
                item.setQuantity(itemDto.getQuantity());
                item.setUnitPrice(itemDto.getUnitPrice());
                item.setSubtotal(itemDto.getSubtotal());

                // Relacionar servicio
                ServiceModel service = serviceRepository.findById(itemDto.getServiceId())
                        .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));
                item.setService(service);

                return item;
            }).collect(Collectors.toList());

            quotationItemRepository.saveAll(items);
        }

        // Devolver DTO actualizado
        return getQuotationById(quotation.getQuotationId());
    }

    @Override
    public List<QuotationDto> getAllQuotations() {
        return quotationRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuotationDto getQuotationById(Integer id) {
        return quotationRepository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public List<QuotationDto> getQuotationsByPatientId(Integer patientId) {
        return quotationRepository.findByPatientPatientId(patientId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Conversión de entidad a DTO
    private QuotationDto toDto(Quotation quotation) {
        QuotationDto dto = new QuotationDto();
        dto.setQuotationId(quotation.getQuotationId());
        dto.setPatientId(quotation.getPatient().getPatientId());
        dto.setHistoryId(quotation.getHistory() != null ? quotation.getHistory().getHistoryId() : null);
        dto.setTotalAmount(quotation.getTotalAmount());
        dto.setStatus(quotation.getStatus());
        dto.setCreatedAt(quotation.getCreatedAt());

        // Items
        List<QuotationItemDto> items = quotationItemRepository.findByQuotation(quotation).stream()
                .map(item -> {
                    QuotationItemDto itemDto = new QuotationItemDto();
                    itemDto.setItemId(item.getItemId());
                    itemDto.setServiceId(item.getService().getServiceId());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setUnitPrice(item.getUnitPrice());
                    itemDto.setSubtotal(item.getSubtotal());
                    itemDto.setServiceName(item.getService().getName());
                    itemDto.setServiceDescription(item.getService().getDescription());
                    return itemDto;
                }).collect(Collectors.toList());
        dto.setItems(items);

        return dto;
    }
} 