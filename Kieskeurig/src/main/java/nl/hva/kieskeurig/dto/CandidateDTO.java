package nl.hva.kieskeurig.dto;

public record CandidateDTO(
    int candidateIdentifier,
    String initials,
    String firstName,
    String namePrefix,
    String lastName,
    int affiliationIdentifier,
    String registeredName,
    String electionIdentifier,
    String electionCategory,
    String electionName,
    String electionDate
){
}
