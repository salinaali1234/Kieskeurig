package nl.hva.kieskeurig.model;

import lombok.Getter;

@Getter
public class CandidateForPartyInfo {
    private final int id;
    private final int candidateId;
    private final String initials;
    private final String firstName;
    private final String namePrefix;
    private final String lastName;
    private final String gender;
    private final String localityName;


    public CandidateForPartyInfo(int id, int candidateId, String initials, String firstName,
                                 String namePrefix, String lastName, String gender, String localityName) {
        this.id = id;
        this.candidateId = candidateId;
        this.initials = initials;
        this.firstName = firstName;
        this.namePrefix = namePrefix;
        this.lastName = lastName;
        this.gender = gender;
        this.localityName = localityName;
    }


//    public Candidate(int id, int candidateId, String initials, String firstName,
//                     String lastName, String gender, String localityName) {
//        this(id, candidateId, initials, firstName, "", lastName, gender, localityName);
//    }


    public CandidateForPartyInfo(int id, int candidateId, String firstName,
                                 String lastName, String gender, String localityName) {
        this(id, candidateId, "", firstName, "", lastName, gender, localityName);
    }
}
