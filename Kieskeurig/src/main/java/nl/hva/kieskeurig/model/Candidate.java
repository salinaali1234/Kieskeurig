package nl.hva.kieskeurig.model;

import lombok.Getter;

@Getter
public class Candidate {
    private final int id;
    private final String initials;
    private final String firstName;
    private final String namePrefix;
    private final String lastName;
    private final String gender;
    private final String localityName;

    public Candidate(int id, String initials, String firstName, String namePrefix, String lastName, String gender, String localityName) {
        this.id = id;
        this.initials = initials;
        this.firstName = firstName;
        this.namePrefix = namePrefix;
        this.lastName = lastName;
        this.gender = gender;
        this.localityName = localityName;
    }

    // Second constructor if the person doesn't have a namePrefix
    public Candidate(int id, String initials, String firstName, String lastName, String gender, String localityName) {
        this(id, initials, firstName, "", lastName, gender, localityName);
    }


}
