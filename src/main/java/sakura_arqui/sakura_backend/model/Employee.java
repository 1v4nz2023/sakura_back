package sakura_arqui.sakura_backend.model;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer doc_number;
    private String first_name;
    private String last_name;
    private String specialty;
    private LocalDateTime hired_at;
    private String phone;
    private String email;
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType tipoDocumento;

    @ManyToOne
    @JoinColumn(name = "job_title_id", nullable = false)
    private JobTitles jobTitle;

    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @OneToOne(mappedBy = "employee")
    private User user;
}
