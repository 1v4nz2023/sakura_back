package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.Rol;
import sakura_arqui.sakura_backend.model.User;
import sakura_arqui.sakura_backend.model.Employee;
import sakura_arqui.sakura_backend.model.District;
import sakura_arqui.sakura_backend.model.Gender;
import sakura_arqui.sakura_backend.model.JobTitles;
import sakura_arqui.sakura_backend.model.DocumentType;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByUsernameAndIsActiveTrue(String username);
    
    List<User> findByRol(Rol rol);
    
    List<User> findByIsActiveTrue();
    
    @Query("SELECT u FROM User u WHERE u.lastLogin IS NOT NULL ORDER BY u.lastLogin DESC")
    List<User> findUsersWithLastLogin();
    
    @Query("SELECT u FROM User u WHERE u.username LIKE %:searchTerm% OR u.rol = :rol")
    List<User> findByUsernameContainingOrRol(@Param("searchTerm") String searchTerm, @Param("rol") Rol rol);
    
    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByEmployee_District(District district);
    List<User> findByEmployee_Gender(Gender gender);
    List<User> findByEmployee_JobTitle(JobTitles jobTitle);
    List<User> findByEmployee_TipoDocumento(DocumentType tipoDocumento);
} 