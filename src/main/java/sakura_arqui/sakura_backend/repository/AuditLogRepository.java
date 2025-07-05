package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.AuditLog;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
    
    List<AuditLog> findByChangedByUserId(Integer userId);
    
    List<AuditLog> findByChangedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<AuditLog> findByEntityType(String entityType);
    
    @Query("SELECT al FROM AuditLog al WHERE al.entityType = :entityType AND al.entityId = :entityId ORDER BY al.changedAt DESC")
    List<AuditLog> findEntityHistory(@Param("entityType") String entityType, @Param("entityId") Long entityId);
    
    @Query("SELECT al FROM AuditLog al WHERE al.field = :field AND al.changedAt BETWEEN :startDate AND :endDate")
    List<AuditLog> findByFieldAndDateRange(@Param("field") String field, 
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT al FROM AuditLog al WHERE al.changedBy.userId = :userId AND al.changedAt >= :startDate")
    List<AuditLog> findByUserAndDateAfter(@Param("userId") Integer userId, @Param("startDate") LocalDateTime startDate);
} 