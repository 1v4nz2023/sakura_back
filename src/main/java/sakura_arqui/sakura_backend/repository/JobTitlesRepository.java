package sakura_arqui.sakura_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sakura_arqui.sakura_backend.model.JobTitles;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobTitlesRepository extends JpaRepository<JobTitles, Integer> {
    
    List<JobTitles> findByStatusTrue();
    
    Optional<JobTitles> findByName(String name);
    
    List<JobTitles> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT jt FROM JobTitles jt WHERE jt.name LIKE %:searchTerm% OR jt.description LIKE %:searchTerm%")
    List<JobTitles> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    boolean existsByName(String name);
} 