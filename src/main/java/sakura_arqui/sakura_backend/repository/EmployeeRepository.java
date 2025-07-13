package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Employee;
import sakura_arqui.sakura_backend.model.JobTitles;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    
    Optional<Employee> findByDocNumber(Integer docNumber);
    
    boolean existsByDocNumber(Integer docNumber);
    
    boolean existsByEmail(String email);
    
    List<Employee> findByStatusTrue();
    
    long countByStatusTrue();
    
    List<Employee> findByJobTitle(JobTitles jobTitle);
    
    List<Employee> findBySpecialty(String specialty);
    
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.specialty) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Employee> findBySearchTerm(@Param("searchTerm") String searchTerm);
} 