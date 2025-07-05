package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Dentist;

import java.util.List;

@Repository
public interface DentistRepository extends JpaRepository<Dentist, Integer> {
    
    List<Dentist> findByActiveTrue();
    
    List<Dentist> findBySpecialtyContainingIgnoreCase(String specialty);
    
    List<Dentist> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    @Query("SELECT d FROM Dentist d WHERE d.firstName LIKE %:searchTerm% OR d.lastName LIKE %:searchTerm% OR d.specialty LIKE %:searchTerm%")
    List<Dentist> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT DISTINCT d.specialty FROM Dentist d WHERE d.specialty IS NOT NULL")
    List<String> findAllSpecialties();
} 