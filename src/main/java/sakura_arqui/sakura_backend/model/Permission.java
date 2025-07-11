package sakura_arqui.sakura_backend.model;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "permission_id")
    private Integer permissionId;
    private String code;
    private String descripcion;
    private boolean status;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Rol> roles = new HashSet<>();
}
