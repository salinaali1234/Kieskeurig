package nl.hva.kieskeurig.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "national_votes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"partyName", "election_year"})
})

public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partyName;
    private int validVotes;

    @Column(name = "election_year")
    private String year;


    public Vote(String partyName, int validVotes, String year) {
        this.partyName = partyName;
        this.validVotes = validVotes;
        this.year = year;
    }
}
