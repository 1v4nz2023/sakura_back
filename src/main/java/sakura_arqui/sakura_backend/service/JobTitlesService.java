package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.JobTitlesDto;
import sakura_arqui.sakura_backend.model.JobTitles;

import java.util.List;
import java.util.Optional;

public interface JobTitlesService {
    
    List<JobTitles> findAll();
    
    List<JobTitles> findActive();
    
    Optional<JobTitles> findById(Integer id);
    
    Optional<JobTitles> findByName(String name);
    
    JobTitles save(JobTitles jobTitles);
    
    JobTitles update(Integer id, JobTitlesDto jobTitlesDto);
    
    void deleteById(Integer id);
    
    List<JobTitles> findBySearchTerm(String searchTerm);
    
    JobTitles createJobTitles(JobTitlesDto jobTitlesDto);
    
    boolean existsByName(String name);
} 