package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByUsernameAndIsActiveTrue(String username);
    
    List<User> findByRole(User.UserRole role);
    
    List<User> findByIsActiveTrue();
    
    @Query("SELECT u FROM User u WHERE u.lastLogin IS NOT NULL ORDER BY u.lastLogin DESC")
    List<User> findUsersWithLastLogin();
    
    @Query("SELECT u FROM User u WHERE u.username LIKE %:searchTerm% OR u.role = :role")
    List<User> findByUsernameContainingOrRole(@Param("searchTerm") String searchTerm, @Param("role") User.UserRole role);
    
    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);
} 