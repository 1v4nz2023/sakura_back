package sakura_arqui.sakura_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "document_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_type_id")
    private Integer documentTypeId;
    
    @Column(name = "code", unique = true)
    private String code;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "documentType")
    @JsonManagedReference
    private java.util.Set<Employee> employees = new java.util.HashSet<>();

    @OneToMany(mappedBy = "documentType")
    @JsonManagedReference
    private java.util.Set<Patient> patients = new java.util.HashSet<>();
}
