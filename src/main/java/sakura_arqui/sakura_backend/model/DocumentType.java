package sakura_arqui.sakura_backend.model;


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
    private Integer tipodocumentoId;
    private String code;
    private String name;
    private boolean status;

    @OneToMany(mappedBy = "tipoDocumento")
    private java.util.Set<Employee> employees = new java.util.HashSet<>();

    @OneToMany(mappedBy = "tipoDocumento")
    private java.util.Set<Patient> patients = new java.util.HashSet<>();
}
