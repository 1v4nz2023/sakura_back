package sakura_arqui.sakura_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dentists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dentist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dentist_id")
    private Integer dentistId;
    
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;
    
    @Column(name = "specialty", length = 80)
    private String specialty;
    
    @Column(name = "active")
    private Boolean active = true;
} 