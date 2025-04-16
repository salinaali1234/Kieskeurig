package nl.hva.kieskeurig.mapper;

import nl.hva.kieskeurig.dto.CandidateDTO;
import nl.hva.kieskeurig.model.Candidate;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * Maps an instance of {@link Candidate} to {@link CandidateDTO}
 */
@Service
public class CandidateDTOMapper implements Function<Candidate, CandidateDTO> {
    @Override
    public CandidateDTO apply(Candidate candidate) {
        return new CandidateDTO(
                candidate.getCandidateIdentifier(),
                candidate.getInitials(),
                candidate.getFirstName(),
                candidate.getNamePrefix(),
                candidate.getLastName(),
                candidate.getAffiliationIdentifier(),
                candidate.getRegisteredName(),
                candidate.getElectionIdentifier(),
                candidate.getElectionCategory(),
                candidate.getElectionName(),
                candidate.getElectionDate()
        );
    }
}
