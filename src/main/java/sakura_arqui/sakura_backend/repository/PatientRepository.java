package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Patient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    
    Optional<Patient> findByDni(String dni);
    
    List<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    List<Patient> findByEmailContainingIgnoreCase(String email);
    
    List<Patient> findByPhoneContaining(String phone);
    
    @Query("SELECT p FROM Patient p WHERE p.firstName LIKE %:searchTerm% OR p.lastName LIKE %:searchTerm% OR p.dni LIKE %:searchTerm%")
    List<Patient> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT p FROM Patient p WHERE p.createdAt >= :startDate AND p.createdAt <= :endDate")
    List<Patient> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    boolean existsByDni(String dni);
    
    boolean existsByEmail(String email);
} 