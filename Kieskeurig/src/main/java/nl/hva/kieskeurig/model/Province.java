package nl.hva.kieskeurig.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

@Data
@Entity
@Builder
@Table(name = "province")
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "province")
    @JsonManagedReference
    private List<Constituency> constituencies;

    public Province(String name) {
        this.name = name;
    }
}