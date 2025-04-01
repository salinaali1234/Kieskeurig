package nl.hva.kieskeurig.model;

import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Candidate {
    int CandidateIdentifier;
    String Initials;
    String FirstName;
    String NamePrefix;
    String LastName;
    int AffiliationIdentifier;
    String RegisteredName;
    int ContestIdentifier;
    String ContestName;
    String ElectionIdentifier;
    String ElectionCategory;
    String ElectionName;
    String ElectionDate;
}
