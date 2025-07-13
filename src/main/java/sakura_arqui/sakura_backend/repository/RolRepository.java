package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sakura_arqui.sakura_backend.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByName(String name);
    
    // Method to handle potential duplicates
    @Query("SELECT r FROM Rol r WHERE r.name = :name ORDER BY r.roleId ASC")
    List<Rol> findAllByNameOrderById(@Param("name") String name);
} 