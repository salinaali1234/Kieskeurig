package nl.hva.kieskeurig.transformer;

import nl.hva.kieskeurig.model.Candidate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.lang.Integer.parseInt;

/**
 * Transforms candidateData from the XMLParser into an instance of Candidate
 */
@Service
public class CandidateTransformer {
    public static Candidate transformCandidate(Map<String, String> candidateData) {
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
