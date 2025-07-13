package sakura_arqui.sakura_backend.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sakura_arqui.sakura_backend.model.*;
import sakura_arqui.sakura_backend.repository.*;
import sakura_arqui.sakura_backend.repository.CategorieServiceRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategorieServiceRepository categorieServiceRepository;
    private final RolRepository rolRepository;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");
        
        // Clean up any duplicate roles first
        cleanupDuplicateRoles();
        
        initializePaymentMethods();
        initializeUsers();
        initializeCategories();
        initializeServices();
        
        log.info("Data initialization completed successfully!");
    }
    
    private void cleanupDuplicateRoles() {
        log.info("Checking for duplicate roles...");
        // This will be handled by the unique constraint now
        // But we can add additional cleanup logic here if needed
    }
    
    private void initializePaymentMethods() {
        log.info("Checking payment methods...");
        
        List<String> paymentMethods = Arrays.asList(
            "Efectivo",
            "Tarjeta de Crédito",
            "Tarjeta de Débito",
            "Transferencia Bancaria",
            "Yape",
            "Plin"
        );
        
        int createdCount = 0;
        for (String methodName : paymentMethods) {
            if (!paymentMethodRepository.existsByName(methodName)) {
                PaymentMethod method = new PaymentMethod();
                method.setName(methodName);
                paymentMethodRepository.save(method);
                createdCount++;
            }
        }
        
        if (createdCount > 0) {
            log.info("Payment methods initialized: {} new methods created", createdCount);
        } else {
            log.info("All payment methods already exist");
        }
    }
    
    private void initializeUsers() {
        // Check if admin user already exists
        if (!userRepository.existsByUsername("admin")) {
            log.info("Initializing users...");
            
            // Handle potential duplicate roles
            Rol adminRol = rolRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    // If no ADMIN role exists, create one
                    Rol newRol = new Rol();
                    newRol.setName("ADMIN");
                    newRol.setDescripcion("Administrador");
                    newRol.setStatus(true);
                    return rolRepository.save(newRol);
                });
            
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@sakura.com");
            adminUser.setPasswordHash(passwordEncoder.encode("admin123"));
            adminUser.setRol(adminRol);
            adminUser.setIsActive(true);
            userRepository.save(adminUser);
            log.info("Admin user created: admin/admin123");
        } else {
            log.info("Admin user already exists, skipping user initialization");
        }
    }
    

    
    private void initializeCategories() {
        log.info("Checking categories...");
        
        List<CategorieService> categories = Arrays.asList(
            new CategorieService(null, "Diagnóstico", "Servicios de diagnóstico dental", true, new java.util.HashSet<>()),
            new CategorieService(null, "Restaurativo", "Servicios restaurativos", true, new java.util.HashSet<>()),
            new CategorieService(null, "Preventivo", "Servicios preventivos", true, new java.util.HashSet<>()),
            new CategorieService(null, "Rehabilitación", "Servicios de rehabilitación", true, new java.util.HashSet<>()),
            new CategorieService(null, "Endodoncia", "Servicios de endodoncia", true, new java.util.HashSet<>()),
            new CategorieService(null, "Odontopediatría", "Servicios para niños", true, new java.util.HashSet<>()),
            new CategorieService(null, "Cirugía", "Servicios quirúrgicos", true, new java.util.HashSet<>()),
            new CategorieService(null, "Estética", "Servicios estéticos", true, new java.util.HashSet<>()),
            new CategorieService(null, "Periodoncia", "Servicios periodontales", true, new java.util.HashSet<>()),
            new CategorieService(null, "Otros", "Servicios Otros", true, new java.util.HashSet<>())
        );
        
        int createdCount = 0;
        for (CategorieService category : categories) {
            if (!categorieServiceRepository.findByName(category.getName()).isPresent()) {
                categorieServiceRepository.save(category);
                createdCount++;
            }
        }
        
        if (createdCount > 0) {
            log.info("Categories initialized: {} new categories created", createdCount);
        } else {
            log.info("All categories already exist");
        }
    }
    
    private void initializeServices() {
        if (serviceRepository.count() == 0) {
            log.info("Initializing services...");
            CategorieService diagnostico = categorieServiceRepository.findByName("Diagnóstico").orElseThrow();
            CategorieService restaurativo = categorieServiceRepository.findByName("Restaurativo").orElseThrow();
            CategorieService preventivo = categorieServiceRepository.findByName("Preventivo").orElseThrow();
            CategorieService rehabilitacion = categorieServiceRepository.findByName("Rehabilitación").orElseThrow();
            CategorieService endodoncia = categorieServiceRepository.findByName("Endodoncia").orElseThrow();
            CategorieService odontopediatria = categorieServiceRepository.findByName("Odontopediatría").orElseThrow();
            CategorieService cirugia = categorieServiceRepository.findByName("Cirugía").orElseThrow();
            CategorieService estetica = categorieServiceRepository.findByName("Estética").orElseThrow();
            CategorieService periodoncia = categorieServiceRepository.findByName("Periodoncia").orElseThrow();
            CategorieService otros = categorieServiceRepository.findByName("Otros").orElseThrow();
            List<Service> services = Arrays.asList(
                createService("Mock Up", "Prueba estética temporal para visualizar cambios.", new BigDecimal("25"), diagnostico),
                createService("Diseño Digital", "Planificación digital de la sonrisa.", new BigDecimal("500"), diagnostico),
                createService("Resina Simple", "Empaste básico para caries superficiales.", new BigDecimal("140"), restaurativo),
                createService("Resina Compuesta", "Restauración de daño moderado con resina estética.", new BigDecimal("220"), restaurativo),
                createService("Resina Compleja", "Reconstrucción dental con múltiples superficies.", new BigDecimal("330"), restaurativo),
                createService("Ionómero Simple", "Relleno dental con liberación de flúor.", new BigDecimal("150"), restaurativo),
                createService("Ionómero Compuesto", "Empaste resistente con componentes de ionómero.", new BigDecimal("220"), restaurativo),
                createService("Sellantes", "Barreras protectoras en molares para prevenir caries.", new BigDecimal("55"), preventivo),
                createService("Profilaxis y Destartraje Simple", "Limpieza dental para remover sarro leve.", new BigDecimal("150"), preventivo),
                createService("Profilaxis y destartraje Moderado (2 citas)", "Limpieza dental profunda en dos sesiones.", new BigDecimal("220"), preventivo),
                createService("Profilaxis y destartraje Moderado (3citas)", "Limpieza intensiva en tres citas.", new BigDecimal("550"), preventivo),
                createService("Aplicación de cloruro de amino de plata", "Detención de caries activas en dientes temporales.", new BigDecimal("80"), preventivo),
                createService("Flúor Barniz", "Refuerzo del esmalte dental con barniz fluorado.", new BigDecimal("90"), preventivo),
                createService("Perno Fibra de Vidrio", "Refuerzo interno estético para dientes tratados.", new BigDecimal("350"), rehabilitacion),
                createService("Perno Metálico", "Refuerzo interno metálico para dientes debilitados.", new BigDecimal("280"), rehabilitacion),
                createService("Apertura Cameral", "Acceso al interior dental para tratamiento.", new BigDecimal("150"), endodoncia),
                createService("Apiceptomía de Dientes Anteriores", "Cirugía para remover raíz infectada en dientes frontales.", new BigDecimal("1350"), endodoncia),
                createService("Apiceptomía de Dientes posteriores", "Cirugía para remover raíz en molares afectados.", new BigDecimal("1350"), endodoncia),
                createService("5 anterior", "Tratamiento de conducto en incisivos y caninos.", new BigDecimal("470"), endodoncia),
                createService("5 Premolar", "Tratamiento de conducto en premolares.", new BigDecimal("600"), endodoncia),
                createService("5 Molar", "Tratamiento de conducto en molares.", new BigDecimal("650"), endodoncia),
                createService("Retratamiento Anterior", "Repetición del tratamiento de conducto en dientes frontales.", new BigDecimal("650"), endodoncia),
                createService("Retratamiento premolar", "Repetición del tratamiento de conducto en premolares.", new BigDecimal("720"), endodoncia),
                createService("Retratamiento Molar", "Repetición del tratamiento de conducto en molares.", new BigDecimal("820"), endodoncia),
                createService("Pulpectomía", "Eliminación de pulpa dental infectada en niños.", new BigDecimal("380"), odontopediatria),
                createService("Pulpotomía", "Remoción parcial de la pulpa dental en niños.", new BigDecimal("280"), odontopediatria),
                createService("Incrustación Resina", "Restauración estética incrustada con resina.", new BigDecimal("470"), restaurativo),
                createService("Incrustación Cerómero", "Restauración estética con material cerómero.", new BigDecimal("720"), restaurativo),
                createService("Incrustación Zirconio", "Restauración altamente estética y resistente.", new BigDecimal("1320"), restaurativo),
                createService("Mucocele", "Extracción de quiste mucoso en la boca.", new BigDecimal("350"), cirugia),
                createService("Curetaje de Alveolo", "Limpieza del alveolo tras extracción dental.", new BigDecimal("200"), cirugia),
                createService("Exodoncia Simple", "Extracción sin complicaciones de una pieza dental.", new BigDecimal("220"), cirugia),
                createService("Exodoncia diente deciduo", "Extracción de diente temporal en niños.", new BigDecimal("100"), cirugia),
                createService("Exodoncia con Colgajo", "Extracción quirúrgica con apertura de encía.", new BigDecimal("300"), cirugia),
                createService("Exodoncia Semi impactada", "Extracción de diente parcialmente cubierto.", new BigDecimal("380"), cirugia),
                createService("Exodoncia Impactada", "Extracción de diente totalmente cubierto o inclinado.", new BigDecimal("660"), cirugia),
                createService("Blanqueamiento unitario", "Aclaramiento de un solo diente.", new BigDecimal("450"), estetica),
                createService("Blanqueamiento con Cubetas", "Aclaramiento con férulas en casa.", new BigDecimal("350"), estetica),
                createService("Blanqueamiento consultorio(2 sesiones)", "Aclaramiento profesional en dos citas.", new BigDecimal("500"), estetica),
                createService("Blanqueamiento Mixto", "Combinación de blanqueamiento en casa y consultorio.", new BigDecimal("660"), estetica),
                createService("Carilla Resina Directa", "Lámina estética aplicada directamente en clínica.", new BigDecimal("380"), restaurativo),
                createService("Carilla Resina Indirecta", "Carilla elaborada en laboratorio y luego adherida.", new BigDecimal("660"), restaurativo),
                createService("Carilla Cerómero", "Carilla estética con cerómero de alta calidad.", new BigDecimal("950"), estetica),
                createService("Carilla Cerámica Emax", "Carilla estética de cerámica altamente resistente.", new BigDecimal("1350"), estetica),
                createService("Corona Acrílico Rápido", "Corona provisional rápida en acrílico.", new BigDecimal("150"), rehabilitacion),
                createService("Corona Acrílico Lento", "Corona provisional de mejor calidad.", new BigDecimal("250"), rehabilitacion),
                createService("Corona Veener", "Corona estética con mínima reducción dental.", new BigDecimal("530"), rehabilitacion),
                createService("Corona Metal Cerámica", "Corona resistente con estructura metálica.", new BigDecimal("850"), rehabilitacion),
                createService("Corona Metal Cerámica con atache", "Corona con sistema de retención adicional.", new BigDecimal("1250"), rehabilitacion),
                createService("Corona Zirconio", "Corona estética sin metal de alta resistencia.", new BigDecimal("1550"), rehabilitacion),
                createService("Prótesis Parcial Removible Wipla o Inmediata", "Prótesis removible de uso inmediato tras extracciones.", new BigDecimal("630"), rehabilitacion),
                createService("Prótesis Parcial Removible Cromo cobalto con dientes de Acrilico", "Prótesis parcial metálica con dientes acrílicos.", new BigDecimal("1200"), rehabilitacion),
                createService("Prótesis Parcial Removible Cromo cobalto con dientes de Resina", "Prótesis parcial metálica con dientes de resina estética.", new BigDecimal("1600"), restaurativo),
                createService("Prótesis Parcial Removible Cromo cobalto con dientes de Atache", "Prótesis parcial con sistema de retención tipo atache.", new BigDecimal("1980"), rehabilitacion),
                createService("Prótesis Parcial Removible Flexible", "Prótesis removible sin metal, flexible y estética.", new BigDecimal("1980"), rehabilitacion),
                createService("Prótesis Total de Acrílico", "Prótesis completa de acrílico para ambas arcadas.", new BigDecimal("1200"), rehabilitacion),
                createService("Prótesis Total de Resina", "Prótesis total con mayor estética y resistencia.", new BigDecimal("1600"), restaurativo),
                createService("Prótesis Total Acrílico triplex, Dientes de Resina y Malla", "Prótesis total reforzada con malla y dientes estéticos.", new BigDecimal("1980"), restaurativo),
                createService("Prótesis Total Rebase de Prótesis", "Ajuste interno de prótesis para mejor adaptación.", new BigDecimal("550"), rehabilitacion),
                createService("Prótesis Total Overdent", "Prótesis total anclada a implantes.", new BigDecimal("2000"), rehabilitacion),
                createService("Férula Miorrelajante Láminas", "Férula para aliviar tensión mandibular leve.", new BigDecimal("350"), rehabilitacion),
                createService("Férula Miorrelajante Acrílico", "Férula rígida para trastornos de ATM.", new BigDecimal("500"), rehabilitacion),
                createService("Alargamiento de Corona Clínica con Osteotomía por pieza", "Cirugía para exponer más superficie dental.", new BigDecimal("550"), rehabilitacion),
                createService("Colgajo Desplazado Coronal (por diente)", "Cirugía periodontal para cubrir recesiones.", new BigDecimal("900"), rehabilitacion),
                createService("Colgajo Desplazado Lateral (por diente)", "Técnica quirúrgica para corregir encías retraídas.", new BigDecimal("900"), periodoncia),
                createService("Colgajo Periodontal", "Acceso quirúrgico para limpieza profunda.", new BigDecimal("900"), periodoncia),
                createService("Fase de Mantenimiento periodontal", "Control periódico para pacientes periodontales.", new BigDecimal("550"), periodoncia),
                createService("Ferulizaciones con Resina (por arcada)", "Unión de dientes móviles con resina.", new BigDecimal("700"), restaurativo),
                createService("Gingivectomía/Gingivoplastía por pieza", "Remodelación de encía en un diente.", new BigDecimal("700"), periodoncia),
                createService("Gingivectomía/Gingivoplastía por Sextante", "Remodelación de encía en un grupo dental.", new BigDecimal("2000"), periodoncia),
                createService("Injerto Gingival", "Trasplante de encía para cubrir raíces.", new BigDecimal("1200"), periodoncia),
                createService("Raspado y Alisado por cuadrante", "Limpieza profunda de encías en un sector.", new BigDecimal("1500"), periodoncia),
                createService("Regeneración Tisular Guiada", "Técnica para recuperar tejido óseo o encía.", new BigDecimal("2000"), otros),
                createService("Recuperación de espacio biológico", "Cirugía para restablecer el espacio encía-hueso.", new BigDecimal("350"), periodoncia)
            );
            serviceRepository.saveAll(services);
            log.info("Services initialized: {}", serviceRepository.count());
        }
    }
    
    private Service createService(String name, String description, BigDecimal basePrice, CategorieService category) {
        Service service = new Service();
        service.setName(name);
        service.setDescription(description);
        service.setBasePrice(basePrice);
        service.setStatus(true);
        service.setCategory(category);
        return service;
    }
    
} 