package nl.hva.kieskeurig.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString(exclude = "province")
@NoArgsConstructor
@AllArgsConstructor

@Data
@Entity
@Builder
@Table(name = "constituency")
public class Constituency {
    @Id
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "province_id")
    @JsonBackReference
    private Province province;

}
