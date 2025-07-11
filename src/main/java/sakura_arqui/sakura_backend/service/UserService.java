package sakura_arqui.sakura_backend.service;

import sakura_arqui.sakura_backend.dto.UserDto;
import sakura_arqui.sakura_backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    List<User> findAll();
    
    Optional<User> findById(Integer id);
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    User save(User user);
    
    User update(Integer id, UserDto userDto);
    
    void deleteById(Integer id);
    
    List<User> findByRole(Integer rolId);
    
    List<User> findActiveUsers();
    
    boolean existsByUsername(String username);
    
    void updateLastLogin(Integer userId);
    
    User createUser(UserDto userDto);
    
    boolean changePassword(Integer userId, String oldPassword, String newPassword);
} 