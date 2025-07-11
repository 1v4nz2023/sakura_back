package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sakura_arqui.sakura_backend.model.CategorieService;
import java.util.Optional;

public interface CategorieServiceRepository extends JpaRepository<CategorieService, Integer> {
    Optional<CategorieService> findByName(String name);
} 