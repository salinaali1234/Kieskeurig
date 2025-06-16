package nl.hva.kieskeurig.repository;

import lombok.Getter;
import nl.hva.kieskeurig.model.Candidate;
import nl.hva.kieskeurig.model.CandidateForPartyInfo;
import nl.hva.kieskeurig.service.CandidateService;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the data of {@link CandidateService}
 */
@Getter
@Repository
public class CandidateRepo {
    private final List<Candidate> candidates = new ArrayList<>();

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
    }
}
