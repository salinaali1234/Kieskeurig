package nl.hva.kieskeurig.mapper;

import nl.hva.kieskeurig.model.Candidate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

/**
 * Maps candidateData from the XMLParser to an instance of {@link Candidate}
 */
@Service
public class CandidateMapper implements Function<Map<String, String>, Candidate> {
    @Override
    public Candidate apply(Map<String, String> candidateData) {
        return new Candidate(
                parseInt(candidateData.get("CandidateIdentifier")),
                candidateData.get("Initials"),
                candidateData.get("FirstName"),
                candidateData.get("NamePrefix"),
                candidateData.get("LastName"),
                parseInt(candidateData.get("AffiliationIdentifier")),
                candidateData.get("RegisteredName"),
                parseInt(candidateData.get("ContestIdentifier")),
                candidateData.get("ContestName"),
                candidateData.get("ElectionIdentifier"),
                candidateData.get("ElectionCategory"),
                candidateData.get("ElectionName"),
                candidateData.get("ElectionDate")
        );
    }
}
