package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakura_arqui.sakura_backend.dto.JobTitlesDto;
import sakura_arqui.sakura_backend.exception.ResourceNotFoundException;
import sakura_arqui.sakura_backend.model.JobTitles;
import sakura_arqui.sakura_backend.repository.JobTitlesRepository;
import sakura_arqui.sakura_backend.service.JobTitlesService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobTitlesServiceImpl implements JobTitlesService {
    
    private final JobTitlesRepository jobTitlesRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<JobTitles> findAll() {
        return jobTitlesRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<JobTitles> findActive() {
        return jobTitlesRepository.findByStatusTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<JobTitles> findById(Integer id) {
        return jobTitlesRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<JobTitles> findByName(String name) {
        return jobTitlesRepository.findByName(name);
    }
    
    @Override
    public JobTitles save(JobTitles jobTitles) {
        return jobTitlesRepository.save(jobTitles);
    }
    
    @Override
    public JobTitles update(Integer id, JobTitlesDto jobTitlesDto) {
        JobTitles jobTitles = jobTitlesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobTitles not found with id: " + id));
        
        jobTitles.setName(jobTitlesDto.getName());
        jobTitles.setDescription(jobTitlesDto.getDescription());
        jobTitles.setStatus(jobTitlesDto.isStatus());
        
        return jobTitlesRepository.save(jobTitles);
    }
    
    @Override
    public void deleteById(Integer id) {
        JobTitles jobTitles = jobTitlesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobTitles not found with id: " + id));
        
        // Soft delete - set status to false
        jobTitles.setStatus(false);
        jobTitlesRepository.save(jobTitles);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<JobTitles> findBySearchTerm(String searchTerm) {
        return jobTitlesRepository.findBySearchTerm(searchTerm);
    }
    
    @Override
    public JobTitles createJobTitles(JobTitlesDto jobTitlesDto) {
        if (jobTitlesRepository.existsByName(jobTitlesDto.getName())) {
            throw new IllegalArgumentException("JobTitles with name " + jobTitlesDto.getName() + " already exists");
        }
        
        JobTitles jobTitles = new JobTitles();
        jobTitles.setName(jobTitlesDto.getName());
        jobTitles.setDescription(jobTitlesDto.getDescription());
        jobTitles.setStatus(jobTitlesDto.isStatus());
        
        return jobTitlesRepository.save(jobTitles);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return jobTitlesRepository.existsByName(name);
    }
} 