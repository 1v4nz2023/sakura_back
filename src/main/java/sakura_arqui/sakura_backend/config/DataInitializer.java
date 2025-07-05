package sakura_arqui.sakura_backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sakura_arqui.sakura_backend.model.*;
import sakura_arqui.sakura_backend.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final DentistRepository dentistRepository;
    private final ServiceRepository serviceRepository;
    private final PromoRepository promoRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        
        initializePaymentMethods();
        initializeUsers();
        initializeDentists();
        initializeServices();
        initializePromos();
        
        log.info("Data initialization completed successfully!");
    }
    
    private void initializePaymentMethods() {
        if (paymentMethodRepository.count() == 0) {
            log.info("Initializing payment methods...");
            
            List<String> paymentMethods = Arrays.asList(
                "Efectivo",
                "Tarjeta de Crédito",
                "Tarjeta de Débito",
                "Transferencia Bancaria",
                "Yape",
                "Plin"
            );
            
            paymentMethods.forEach(methodName -> {
                PaymentMethod method = new PaymentMethod();
                method.setName(methodName);
                paymentMethodRepository.save(method);
            });
            
            log.info("Payment methods initialized: {}", paymentMethodRepository.count());
        }
    }
    
    private void initializeUsers() {
        if (userRepository.count() == 0) {
            log.info("Initializing users...");
            
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPasswordHash(passwordEncoder.encode("admin123"));
            adminUser.setRole(User.UserRole.ADMIN);
            adminUser.setIsActive(true);
            
            userRepository.save(adminUser);
            
            log.info("Admin user created: admin/admin123");
        }
    }
    
    private void initializeDentists() {
        if (dentistRepository.count() == 0) {
            log.info("Initializing dentists...");
            
            List<Dentist> dentists = Arrays.asList(
                createDentist("Dr. María", "González", "Ortodoncista"),
                createDentist("Dr. Carlos", "Rodríguez", "Endodoncista"),
                createDentist("Dra. Ana", "López", "Periodoncista"),
                createDentist("Dr. Luis", "Martínez", "Cirujano Oral")
            );
            
            dentistRepository.saveAll(dentists);
            log.info("Dentists initialized: {}", dentistRepository.count());
        }
    }
    
    private Dentist createDentist(String firstName, String lastName, String specialty) {
        Dentist dentist = new Dentist();
        dentist.setFirstName(firstName);
        dentist.setLastName(lastName);
        dentist.setSpecialty(specialty);
        dentist.setActive(true);
        return dentist;
    }
    
    private void initializeServices() {
        if (serviceRepository.count() == 0) {
            log.info("Initializing services...");
            
            List<Service> services = Arrays.asList(
                createService("Limpieza Dental", "Limpieza profesional y profilaxis", new BigDecimal("80.00")),
                createService("Empaste Dental", "Tratamiento de caries con composite", new BigDecimal("120.00")),
                createService("Extracción Simple", "Extracción de diente sin complicaciones", new BigDecimal("150.00")),
                createService("Extracción Compleja", "Extracción de diente con cirugía", new BigDecimal("300.00")),
                createService("Ortodoncia Inicial", "Consulta y plan de tratamiento", new BigDecimal("200.00")),
                createService("Blanqueamiento Dental", "Tratamiento de blanqueamiento profesional", new BigDecimal("250.00")),
                createService("Endodoncia", "Tratamiento de conducto", new BigDecimal("400.00")),
                createService("Corona Dental", "Corona de porcelana", new BigDecimal("800.00")),
                createService("Implante Dental", "Implante con corona", new BigDecimal("2500.00")),
                createService("Radiografía Panorámica", "Radiografía completa de la boca", new BigDecimal("100.00"))
            );
            
            serviceRepository.saveAll(services);
            log.info("Services initialized: {}", serviceRepository.count());
        }
    }
    
    private Service createService(String name, String description, BigDecimal basePrice) {
        Service service = new Service();
        service.setName(name);
        service.setDescription(description);
        service.setBasePrice(basePrice);
        return service;
    }
    
    private void initializePromos() {
        if (promoRepository.count() == 0) {
            log.info("Initializing promos...");
            
            List<Promo> promos = Arrays.asList(
                createPromo("Descuento Estudiantes", new BigDecimal("15.00"), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)),
                createPromo("Primera Visita", new BigDecimal("20.00"), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)),
                createPromo("Pacientes Frecuentes", new BigDecimal("10.00"), LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))
            );
            
            promoRepository.saveAll(promos);
            log.info("Promos initialized: {}", promoRepository.count());
        }
    }
    
    private Promo createPromo(String name, BigDecimal discountPct, LocalDate activeFrom, LocalDate activeTo) {
        Promo promo = new Promo();
        promo.setName(name);
        promo.setDiscountPct(discountPct);
        promo.setActiveFrom(activeFrom);
        promo.setActiveTo(activeTo);
        return promo;
    }
} 