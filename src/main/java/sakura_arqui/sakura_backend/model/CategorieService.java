package sakura_arqui.sakura_backend.model;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categorie_service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categorie_service_id")
    private Integer categorieServiceId;
    private String name;
    private String description;
    private boolean status;

        // -----------------------------------
    // Una categoría tiene MUCHOS servicios
    // -----------------------------------
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<ServiceModel> services = new HashSet<>();
}
