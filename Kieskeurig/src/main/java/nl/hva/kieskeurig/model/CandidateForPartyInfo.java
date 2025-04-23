package nl.hva.kieskeurig.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateForPartyInfo {
    private final int id;
    private final int candidateId;
    private final String initials;
    private final String firstName;
    private final String namePrefix;
    private final String lastName;
    private final String gender;
    private final String localityName;
    private boolean elected;

    public CandidateForPartyInfo(int id, int candidateId, String initials, String firstName,
                                 String namePrefix, String lastName, String gender,
                                 String localityName, boolean elected) {
        this.id = id;
        this.candidateId = candidateId;
        this.initials = initials;
        this.firstName = firstName;
        this.namePrefix = namePrefix;
        this.lastName = lastName;
        this.gender = gender;
        this.localityName = localityName;
        this.elected = elected;
    }

    // Existing constructors should be updated to include elected status
    public CandidateForPartyInfo(int id, int candidateId, String firstName,
                                 String lastName, String gender, String localityName, boolean elected) {
        this(id, candidateId, "", firstName, "", lastName, gender, localityName, elected);
    }
     public void setElected(boolean isElected) {
        this.elected = isElected;
    }
}