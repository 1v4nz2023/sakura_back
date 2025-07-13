package sakura_arqui.sakura_backend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "district")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private Integer districtId;
    private String name;
    private boolean status;

    @OneToMany(mappedBy = "district")
    @JsonManagedReference
    private java.util.Set<Employee> employees = new java.util.HashSet<>();

    @OneToMany(mappedBy = "district")
    @JsonManagedReference
    private java.util.Set<Patient> patients = new java.util.HashSet<>();
}
