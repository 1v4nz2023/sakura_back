package sakura_arqui.sakura_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "gender")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gender_id")
    private Integer genderId;
    private String code;
    private String name;
    private boolean status;

    @OneToMany(mappedBy = "gender")
    @JsonManagedReference
    private java.util.Set<Employee> employees = new java.util.HashSet<>();

    @OneToMany(mappedBy = "gender")
    @JsonManagedReference
    private java.util.Set<Patient> patients = new java.util.HashSet<>();
}
