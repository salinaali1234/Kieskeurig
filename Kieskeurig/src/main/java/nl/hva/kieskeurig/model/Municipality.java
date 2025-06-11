package nl.hva.kieskeurig.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "constituency")
@NoArgsConstructor
@AllArgsConstructor

@Data
@Entity
@Builder
@Table(name = "municipality")
public class Municipality {
    @Id
    private Integer Id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "constituency_id")
    @JsonBackReference
    private Constituency constituency;
}
