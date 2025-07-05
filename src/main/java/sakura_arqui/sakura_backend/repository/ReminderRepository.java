package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Reminder;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    
    List<Reminder> findByStatus(Reminder.ReminderStatus status);
    
    List<Reminder> findByQuotationQuotationId(Integer quotationId);
    
    List<Reminder> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Reminder> findByDueDateBeforeAndStatus(LocalDate date, Reminder.ReminderStatus status);
    
    @Query("SELECT r FROM Reminder r WHERE r.dueDate <= :currentDate AND r.status = 'PENDIENTE'")
    List<Reminder> findOverdueReminders(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT r FROM Reminder r WHERE r.quotation.patient.firstName LIKE %:searchTerm% OR r.quotation.patient.lastName LIKE %:searchTerm%")
    List<Reminder> findByPatientSearchTerm(@Param("searchTerm") String searchTerm);
} 