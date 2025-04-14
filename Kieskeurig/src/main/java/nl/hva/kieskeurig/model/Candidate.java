package nl.hva.kieskeurig.model;

import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Candidate {
    int candidateIdentifier;
    String initials;
    String firstName;
    String namePrefix;
    String lastName;
    int affiliationIdentifier;
    String registeredName;
    int contestIdentifier;
    String contestName;
    String electionIdentifier;
    String electionCategory;
    String electionName;
    String electionDate;
}
