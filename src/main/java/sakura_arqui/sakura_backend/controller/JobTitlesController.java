package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.JobTitlesDto;
import sakura_arqui.sakura_backend.model.JobTitles;
import sakura_arqui.sakura_backend.service.JobTitlesService;

import java.util.List;

@RestController
@RequestMapping("/api/job-titles")
@RequiredArgsConstructor
public class JobTitlesController {
    
    private final JobTitlesService jobTitlesService;
    
    @GetMapping
    public ResponseEntity<List<JobTitles>> getAllJobTitles() {
        List<JobTitles> jobTitles = jobTitlesService.findAll();
        return ResponseEntity.ok(jobTitles);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<JobTitles>> getActiveJobTitles() {
        List<JobTitles> jobTitles = jobTitlesService.findActive();
        return ResponseEntity.ok(jobTitles);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<JobTitles> getJobTitlesById(@PathVariable Integer id) {
        return jobTitlesService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<JobTitles> getJobTitlesByName(@PathVariable String name) {
        return jobTitlesService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<JobTitles> createJobTitles(@Valid @RequestBody JobTitlesDto jobTitlesDto) {
        JobTitles createdJobTitles = jobTitlesService.createJobTitles(jobTitlesDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJobTitles);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<JobTitles> updateJobTitles(@PathVariable Integer id, @Valid @RequestBody JobTitlesDto jobTitlesDto) {
        JobTitles updatedJobTitles = jobTitlesService.update(id, jobTitlesDto);
        return ResponseEntity.ok(updatedJobTitles);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobTitles(@PathVariable Integer id) {
        jobTitlesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<JobTitles>> searchJobTitles(@RequestParam String searchTerm) {
        List<JobTitles> jobTitles = jobTitlesService.findBySearchTerm(searchTerm);
        return ResponseEntity.ok(jobTitles);
    }
} 