package sakura_arqui.sakura_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer patientId;
    
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;
    
    @Column(name = "doc_number", unique = true, length = 8)
    private String dni;

    @Column(name = "birth_date")
    private LocalDateTime birthDate;

    @ManyToOne
    @JoinColumn(name = "document_type_id", nullable = false)
    @JsonBackReference
    private DocumentType documentType;

    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    @JsonBackReference
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    @JsonBackReference
    private District district;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "email", length = 120)
    private String email;

    private boolean status;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
} 