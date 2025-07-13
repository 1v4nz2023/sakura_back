package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.EmployeeDto;
import sakura_arqui.sakura_backend.model.Employee;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EmployeeService {
    
    List<Employee> findAll();
    
    Optional<Employee> findById(Integer id);
    
    Optional<Employee> findByDocNumber(Integer docNumber);
    
    Employee save(Employee employee);
    
    Employee update(Integer id, EmployeeDto employeeDto);
    
    void deleteById(Integer id);
    
    List<Employee> findBySearchTerm(String searchTerm);
    
    List<Employee> findByJobTitle(Integer jobTitleId);
    
    List<Employee> findBySpecialty(String specialty);
    
    List<Employee> findByStatusTrue();
    
    boolean existsByDocNumber(Integer docNumber);
    
    boolean existsByEmail(String email);
    
    Employee updateStatus(Integer id, boolean status);
    
    Map<String, Object> getEmployeeStats();
    
    Employee createEmployee(EmployeeDto employeeDto);
} 