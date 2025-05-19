package nl.hva.kieskeurig.model;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor

@Entity
@Data
@NoArgsConstructor
@Builder
@Table(name = "constituencies")
public class Constituency {
    @Id
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

}
