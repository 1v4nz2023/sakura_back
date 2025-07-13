package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakura_arqui.sakura_backend.dto.EmployeeDto;
import sakura_arqui.sakura_backend.exception.ResourceNotFoundException;
import sakura_arqui.sakura_backend.model.DocumentType;
import sakura_arqui.sakura_backend.model.District;
import sakura_arqui.sakura_backend.model.Employee;
import sakura_arqui.sakura_backend.model.Gender;
import sakura_arqui.sakura_backend.model.JobTitles;
import sakura_arqui.sakura_backend.model.Rol;
import sakura_arqui.sakura_backend.model.User;
import sakura_arqui.sakura_backend.repository.DocumentTypeRepository;
import sakura_arqui.sakura_backend.repository.DistrictRepository;
import sakura_arqui.sakura_backend.repository.EmployeeRepository;
import sakura_arqui.sakura_backend.repository.GenderRepository;
import sakura_arqui.sakura_backend.repository.JobTitlesRepository;
import sakura_arqui.sakura_backend.repository.RolRepository;
import sakura_arqui.sakura_backend.repository.UserRepository;
import sakura_arqui.sakura_backend.service.EmployeeService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final JobTitlesRepository jobTitlesRepository;
    private final GenderRepository genderRepository;
    private final DistrictRepository districtRepository;
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findById(Integer id) {
        return employeeRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findByDocNumber(Integer docNumber) {
        return employeeRepository.findByDocNumber(docNumber);
    }
    
    @Override
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    @Override
    public Employee update(Integer id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        
        updateEmployeeFromDto(employee, employeeDto);
        return employeeRepository.save(employee);
    }
    
    @Override
    public void deleteById(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findBySearchTerm(String searchTerm) {
        return employeeRepository.findBySearchTerm(searchTerm);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findByJobTitle(Integer jobTitleId) {
        JobTitles jobTitle = jobTitlesRepository.findById(jobTitleId)
                .orElseThrow(() -> new ResourceNotFoundException("Job title not found with id: " + jobTitleId));
        return employeeRepository.findByJobTitle(jobTitle);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findBySpecialty(String specialty) {
        return employeeRepository.findBySpecialty(specialty);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Employee> findByStatusTrue() {
        return employeeRepository.findByStatusTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByDocNumber(Integer docNumber) {
        return employeeRepository.existsByDocNumber(docNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }
    
    @Override
    public Employee updateStatus(Integer id, boolean status) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        employee.setStatus(status);
        return employeeRepository.save(employee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getEmployeeStats() {
        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByStatusTrue();
        long inactiveEmployees = totalEmployees - activeEmployees;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalEmployees", totalEmployees);
        stats.put("activeEmployees", activeEmployees);
        stats.put("inactiveEmployees", inactiveEmployees);
        
        return stats;
    }
    
    @Override
    public Employee createEmployee(EmployeeDto employeeDto) {
        // Check if employee with same doc number already exists
        if (employeeRepository.existsByDocNumber(employeeDto.getDocNumber())) {
            throw new IllegalArgumentException("Employee with document number " + employeeDto.getDocNumber() + " already exists");
        }
        
        // Check if email is already in use
        if (employeeDto.getEmail() != null && employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new IllegalArgumentException("Email " + employeeDto.getEmail() + " is already in use");
        }
        
        Employee employee = new Employee();
        updateEmployeeFromDto(employee, employeeDto);
        
        // Set default values
        if (employee.getHiredAt() == null) {
            employee.setHiredAt(LocalDateTime.now());
        }
        if (employeeDto.getStatus() == null) {
            employee.setStatus(true);
        }
        
        Employee savedEmployee = employeeRepository.save(employee);
        
        // Create user account if username and password are provided
        if (employeeDto.getUsername() != null && employeeDto.getPassword() != null) {
            createUserAccount(savedEmployee, employeeDto);
        }
        
        return savedEmployee;
    }
    
    private void updateEmployeeFromDto(Employee employee, EmployeeDto employeeDto) {
        employee.setDocNumber(employeeDto.getDocNumber());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setSpecialty(employeeDto.getSpecialty());
        employee.setHiredAt(employeeDto.getHiredAt());
        employee.setPhone(employeeDto.getPhone());
        employee.setEmail(employeeDto.getEmail());
        employee.setStatus(employeeDto.getStatus() != null ? employeeDto.getStatus() : true);
        
        // Set relationships
        if (employeeDto.getDocumentTypeId() != null) {
            DocumentType documentType = documentTypeRepository.findById(employeeDto.getDocumentTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Document type not found with id: " + employeeDto.getDocumentTypeId()));
            employee.setDocumentType(documentType);
        }
        
        if (employeeDto.getJobTitleId() != null) {
            JobTitles jobTitle = jobTitlesRepository.findById(employeeDto.getJobTitleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Job title not found with id: " + employeeDto.getJobTitleId()));
            employee.setJobTitle(jobTitle);
        }
        
        if (employeeDto.getGenderId() != null) {
            Gender gender = genderRepository.findById(employeeDto.getGenderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Gender not found with id: " + employeeDto.getGenderId()));
            employee.setGender(gender);
        }
        
        if (employeeDto.getDistrictId() != null) {
            District district = districtRepository.findById(employeeDto.getDistrictId())
                    .orElseThrow(() -> new ResourceNotFoundException("District not found with id: " + employeeDto.getDistrictId()));
            employee.setDistrict(district);
        }
    }
    
    private void createUserAccount(Employee employee, EmployeeDto employeeDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(employeeDto.getUsername())) {
            throw new IllegalArgumentException("Username " + employeeDto.getUsername() + " already exists");
        }
        
        User user = new User();
        user.setUsername(employeeDto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(employeeDto.getPassword()));
        user.setEmail(employee.getEmail());
        user.setEmployee(employee);
        user.setIsActive(true);
        
        // Set role
        if (employeeDto.getRoleId() != null) {
            Rol role = rolRepository.findById(employeeDto.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + employeeDto.getRoleId()));
            user.setRol(role);
        } else {
            // Default role (you can set a default role here)
            Rol defaultRole = rolRepository.findByName("EMPLOYEE")
                    .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
            user.setRol(defaultRole);
        }
        
        userRepository.save(user);
    }
} 