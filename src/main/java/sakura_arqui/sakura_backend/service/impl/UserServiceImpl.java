package sakura_arqui.sakura_backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakura_arqui.sakura_backend.dto.UserDto;
import sakura_arqui.sakura_backend.exception.ResourceNotFoundException;
import sakura_arqui.sakura_backend.model.Rol;
import sakura_arqui.sakura_backend.model.User;
import sakura_arqui.sakura_backend.repository.RolRepository;
import sakura_arqui.sakura_backend.repository.UserRepository;
import sakura_arqui.sakura_backend.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public User update(Integer id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setUsername(userDto.getUsername());
        if (userDto.getPasswordHash() != null && !userDto.getPasswordHash().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(userDto.getPasswordHash()));
        }
        user.setRol(rolRepository.findById(userDto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol not found with id: " + userDto.getRolId())));
        user.setIsActive(userDto.getIsActive());
        
        return userRepository.save(user);
    }
    
    @Override
    public void deleteById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findByRole(Integer rolId) {
        Rol rol = rolRepository.findById(rolId)
            .orElseThrow(() -> new ResourceNotFoundException("Rol not found with id: " + rolId));
        return userRepository.findByRol(rol);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<User> findActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public void updateLastLogin(Integer userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }
    
    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPasswordHash()));
        user.setRol(rolRepository.findById(userDto.getRolId())
                .orElseThrow(() -> new ResourceNotFoundException("Rol not found with id: " + userDto.getRolId())));
        user.setIsActive(userDto.getIsActive() != null ? userDto.getIsActive() : true);
        
        return userRepository.save(user);
    }
    
    @Override
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        if (passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            user.setPasswordHash(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
} 