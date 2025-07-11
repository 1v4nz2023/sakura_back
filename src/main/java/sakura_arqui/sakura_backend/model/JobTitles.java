package sakura_arqui.sakura_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "job_titles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobTitles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_title_id")
    private Integer jobTitleId;
    private String name;
    private String description;
    private boolean status;

    @OneToMany(mappedBy = "jobTitle")
    private java.util.Set<Employee> employees = new java.util.HashSet<>();
}
