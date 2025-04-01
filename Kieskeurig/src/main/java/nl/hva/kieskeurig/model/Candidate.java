package nl.hva.kieskeurig.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
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
