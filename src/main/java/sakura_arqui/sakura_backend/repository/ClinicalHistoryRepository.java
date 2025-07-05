package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.ClinicalHistory;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClinicalHistoryRepository extends JpaRepository<ClinicalHistory, Integer> {
    
    List<ClinicalHistory> findByPatientPatientId(Integer patientId);
    
    List<ClinicalHistory> findByPatientPatientIdOrderByCreatedAtDesc(Integer patientId);
    
    @Query("SELECT ch FROM ClinicalHistory ch WHERE ch.patient.firstName LIKE %:searchTerm% OR ch.patient.lastName LIKE %:searchTerm%")
    List<ClinicalHistory> findByPatientSearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT ch FROM ClinicalHistory ch WHERE ch.createdAt BETWEEN :startDate AND :endDate")
    List<ClinicalHistory> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
} 