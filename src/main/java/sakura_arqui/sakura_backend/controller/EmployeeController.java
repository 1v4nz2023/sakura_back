package sakura_arqui.sakura_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sakura_arqui.sakura_backend.dto.EmployeeDto;
import sakura_arqui.sakura_backend.model.Employee;
import sakura_arqui.sakura_backend.service.EmployeeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAll();
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
        return employeeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/doc-number/{docNumber}")
    public ResponseEntity<Employee> getEmployeeByDocNumber(@PathVariable Integer docNumber) {
        return employeeService.findByDocNumber(docNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        Employee createdEmployee = employeeService.createEmployee(employeeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Integer id, @Valid @RequestBody EmployeeDto employeeDto) {
        Employee updatedEmployee = employeeService.update(id, employeeDto);
        return ResponseEntity.ok(updatedEmployee);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String searchTerm) {
        List<Employee> employees = employeeService.findBySearchTerm(searchTerm);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/job-title/{jobTitleId}")
    public ResponseEntity<List<Employee>> getEmployeesByJobTitle(@PathVariable Integer jobTitleId) {
        List<Employee> employees = employeeService.findByJobTitle(jobTitleId);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<Employee>> getEmployeesBySpecialty(@PathVariable String specialty) {
        List<Employee> employees = employeeService.findBySpecialty(specialty);
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<Employee>> getActiveEmployees() {
        List<Employee> employees = employeeService.findByStatusTrue();
        return ResponseEntity.ok(employees);
    }
    
    @GetMapping("/exists/doc-number/{docNumber}")
    public ResponseEntity<Boolean> checkDocNumberExists(@PathVariable Integer docNumber) {
        boolean exists = employeeService.existsByDocNumber(docNumber);
        return ResponseEntity.ok(exists);
    }
    
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = employeeService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Employee> updateEmployeeStatus(
            @PathVariable Integer id, 
            @RequestParam boolean status) {
        Employee updatedEmployee = employeeService.updateStatus(id, status);
        return ResponseEntity.ok(updatedEmployee);
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getEmployeeStats() {
        Map<String, Object> stats = employeeService.getEmployeeStats();
        return ResponseEntity.ok(stats);
    }
} 